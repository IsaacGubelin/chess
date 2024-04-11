package ui;
import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import facade.ServerFacade;
import model.*;
import resException.ResponseException;
import webSocket.ServiceMessageHandler;
import webSocket.WebSocketFacade;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.HashMap;
import java.util.Scanner;
import static ui.EscapeSequences.*;


public class ClientUI {

    private final String LOGGED_OUT = "[LOGGED_OUT]";   // Used in pre-login prompts
    private final String LOGGED_IN = "[LOGGED_IN]";     // Used in post-login prompts
    private String status;                              // State of client
    private String name;                                // Username of client
    private String authToken;                           // Keeps track of the user's authToken
    private ChessGame chessGame;                        // Local copy of chess game when player joins/creates one

    // CONNECTIONS AND FACADES
    private final String url;
    private ServiceMessageHandler msgHandler;
    private WebSocketFacade clientSocket;               // Access the methods in the web socket facade
    private final ServerFacade facade;                  // Access the facade methods
    private HashMap<Integer, Integer> gameIDs;                  // Keeps track of listed games

    private int currentGameIndex;

    // Constructor for Client UI object
    public ClientUI(String url) throws ResponseException {
        facade = new ServerFacade(url); // Initialize server facade with given url
        this.url = url;                 // Store url
        initClientUI();                 // Initialize all other variables
    }

    private void initClientUI() throws ResponseException {   // Ordering matters on these initializations
        gameIDs = new HashMap<>();      // Initialize game ID container
        msgHandler = new ServiceMessageHandler() {  // in-line implementation for needed functions
            @Override
            public void notify(String message) {
                handleServerMessage(message);  // This function determines the message type and then does needed tasks
            }
        };
        clientSocket = new WebSocketFacade(url, msgHandler);
        status = LOGGED_OUT;            // Status starts in logged out state
        authToken = "";                 // No token at startup
        currentGameIndex = -1;          // Will be updated to positive value when user joins/observes game
        updateGamesList();              // Check all current chess games and store their IDs in a list
    }

    // The main looping interface that collects user input and prints prompt messages.
    public void runInterface() {
        System.out.print(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK);     // Set welcome color format
        System.out.println("♛ Welcome to COMMAND LINE CHESS! ♛");      // Welcome message
        System.out.println("Type \"help\" to view actions.");           // Prompt user to open help menu

        // Begin main loop of collecting input and performing requested actions.
        while (true) {
            System.out.print(status + " >>> ");                                 // Print status of client
            Scanner scanner = new Scanner(System.in);                           // For reading input
            String line = scanner.nextLine();
            String[] inputs = line.split(" ");                            //  Collect and parse input
            int numArgs = inputs.length;                                        // Evaluate number of input words
            inputs[0] = inputs[0].toLowerCase();                                // Ignore capitals of first argument
            setTextToPromptFormat();                                            // Set text to prompt format

            if (status.equals(LOGGED_OUT)) {
                promptLoggedOut(inputs, numArgs);
            } else {
                promptLoggedIn(inputs, numArgs);
            }

            if (line.equals("quit") && status.equals(LOGGED_OUT)) {
                break;                              // Quit the application when user enters "quit"
            }
        }
    }

    // Prints prompts and evaluates input during logged-out state
    void promptLoggedOut(String[] inputs, int numArgs) {
        // Look at first argument of user's entry. Check following arguments if applicable
        switch (inputs[0]) {
            case "help":
                printHelpScreenLoggedOut();
                break;

            case "register":
                if (numArgs < 2) {      // If user failed to type all required registration info
                    System.out.println("Missing username, password, and email. Use format below:");
                    System.out.println("register <USERNAME> <PASSWORD> <EMAIL>");
                } else if (numArgs < 4) {
                    System.out.println("Missing info. Please try register command again with this format:");
                    System.out.println("register <USERNAME> <PASSWORD> <EMAIL>");
                } else if (numArgs == 4) {
                    UserData userData = new UserData(inputs[1], inputs[2], inputs[3]);  // Generate user data
                    try {
                        var authData = facade.register(userData);  // Call register method in server facade
                        System.out.println("Successful registration for " + inputs[1]);
                        name = inputs[1];                       // Store username
                        authToken = authData.authToken();       // Store token
                        status = LOGGED_IN;     // Status string shows that newly registered user is logged in

                    } catch (ResponseException ex) {
                        System.out.println("Could not register.");
                        System.out.println(ex.getMessage());    // Print thrown error code
                    }
                }
                else {
                    System.out.println("Invalid registration. Please try again.");
                }
                break;

            case "login":
                if (numArgs < 3) {          // Too few arguments
                    System.out.println("Missing username and/or password. Follow below format for login:");
                    System.out.println("login <USERNAME> <PASSWORD>");
                } else if (numArgs > 3) {   // Too many arguments
                    System.out.println("Too many arguments typed in. Follow below format for login:");
                    System.out.println("login <USERNAME> <PASSWORD>");
                } else {    // Correct format, attempt to call server login endpoint
                    UserData userData = new UserData(inputs[1], inputs[2], null); // No email needed for login
                    try {
                        var authData = facade.login(userData);
                        System.out.println("Successful login for " + inputs[1]);
                        name = inputs[1];                   // Store username
                        authToken = authData.authToken();   // Store token
                        status = LOGGED_IN;
                    } catch (ResponseException ex) {
                        System.out.println("Could not log in.");
                        System.out.println(ex.getMessage());        // Print thrown error code
                    }
                }
                break;

            case "quit":
                if (numArgs > 1) {
                    System.out.println("To quit the game, type \"quit\" with no other text.");
                } else {
                    System.out.println("Good bye!");
                }
                break;

            default:
                System.out.println("Invalid input. Follow these options:");
                printHelpScreenLoggedOut();
                break;
        }
    }

    // Prints prompts and evaluates input during logged-in state
    void promptLoggedIn(String[] inputs, int numArgs) {
        // Look at first argument of user's entry. Check following arguments if applicable
        switch (inputs[0]) {

            case "create":
                if (numArgs != 2) {
                    System.out.println("Invalid format for creating game. Follow below format:");
                    System.out.println("create <GAME_NAME>");
                } else {
                    String gameName = inputs[1]; // Collect game name input
                    try {
                        GameIDData idData = facade.createGame(authToken, gameName);
                        System.out.println("Successful game creation. Game ID: " + idData.gameID());
                    } catch (ResponseException ex) {
                        System.out.println("Could not create game.");
                        System.out.println(ex.getMessage());
                    }
                }
                break;

            case "list":
                if (numArgs > 1) {
                    System.out.println("To list games, type \"list\" with no other text or arguments.");
                } else {
                    try {
                        ListGamesData gamesList = facade.getGamesList(authToken);
                        listGamesInfo(gamesList);  // Print info for all active chess games
                    } catch (ResponseException ex) {
                        System.out.println("Could not list games.");
                        System.out.println(ex.getMessage());
                    }
                }

                break;

            case "join":
                if (numArgs != 3) {
                    System.out.println("Invalid entry. To join a game, use below format:");
                    System.out.println("join <ID> [WHITE|BLACK]");
                } else if (!inputs[2].equals("WHITE") && !inputs[2].equals("BLACK")) {
                    System.out.println("Invalid team request. Type \"BLACK\" or \"WHITE\"");
                } else {
                    updateGamesList();  // Update list of games
                    try {
                        int requestedGameIndex = Integer.parseInt(inputs[1]); // Get integer from second argument
                        int id = gameIDs.get(requestedGameIndex);             // Retrieve corresponding game ID
                        GameRequestData gameReqData = new GameRequestData(null, inputs[2], id); // Make req
                        facade.joinGame(authToken, gameReqData);    // Attempt to call facade join method
                        ListGamesData gamesList = facade.getGamesList(authToken);   // Find game for printing
                        ChessBoard displayBoard = gamesList.games().get(id).game().getBoard(); // Get the board
                        ChessBoardPrint.printChessBoard(displayBoard, false);   // Print for black team
                        ChessBoardPrint.printChessBoard(displayBoard, true);   // Print for white team
                        currentGameIndex = requestedGameIndex;      // If joined, update current game index
                        System.out.println("Successfully joined game " + id);

                    } catch (NumberFormatException numEx) { // Prints error message if second argument wasn't a number
                        System.out.println("Please use only integers for ID of requested game.");
                    } catch (ResponseException ex) {
                        System.out.println("Could not join game.");
                        System.out.println(ex.getMessage());
                    } catch (NullPointerException nullEx) {
                        System.out.println("Specified game ID does not exist.");
                    }
                }

                break;

            case "observe":
                if (numArgs != 2) {
                    System.out.println("Invalid entry. To observe a game, use below format:");
                    System.out.println("observe <GAME_ID>");
                } else {
                    updateGamesList();  // Get updated list of available game IDs
                    try {
                        int requestedIndex = Integer.parseInt(inputs[1]);     // Get integer from second argument
                        if (!gameIDs.containsKey(requestedIndex)) {
                            System.out.println("Specified game ID does not exist.");
                        } else {
                            currentGameIndex = requestedIndex;
                            int id = gameIDs.get(currentGameIndex);     // Retrieve actual game ID
                            ListGamesData gamesList = facade.getGamesList(authToken);   // Find game for printing
                            ChessBoard displayBoard = gamesList.games().get(id).game().getBoard(); // Get the board
                            ChessBoardPrint.printChessBoard(displayBoard, false);   // Print black team
                            ChessBoardPrint.printChessBoard(displayBoard, true);   // Print white team
                            System.out.println("Observing game " + currentGameIndex);
                        }
                    } catch (NumberFormatException numEx) {
                        System.out.println("Second argument not a number. Must use integer.");
                    } catch (ResponseException ex) {
                        System.out.println("Could not retrieve game for viewing.");
                        System.out.println(ex.getMessage());
                    }
                }

                break;

            case "logout":
                try {
                    facade.logout(authToken);
                    System.out.println("Logged out.");
                    status = LOGGED_OUT;    // Change status string to logged out state
                } catch (ResponseException ex) {
                    System.out.println("Could not logout.");
                    System.out.println(ex.getMessage());
                }
                break;

            case "help":
                printHelpScreenLoggedIn();
                break;


            default:
                System.out.println("Invalid input. Follow these options:");
                printHelpScreenLoggedIn();
                break;
        }
    }

    // Helper function is used for implementation of ServiceMessageHandler (see client init)
    void handleServerMessage(String message) {
        var js = new Gson();                                                    // Make a Json conversion object
        ServerMessage msg = js.fromJson(message, ServerMessage.class);          // Deserialize into ServerMessage
        ServerMessage.ServerMessageType type = msg.getServerMessageType();      // Determine type of message
        switch (type) {                                             // Choose what to do with message based on type
            case LOAD_GAME -> {
                this.chessGame = js.fromJson(message, LoadGameMessage.class).getGame(); // Update the local game
                System.out.printf("Chess game updated for %s.\n", name);    // Notify user
            }
            case ERROR -> {
                String errMsg = new Gson().fromJson(message, ErrorMessage.class).getMessage();  // Get error message
                System.out.println(SET_TEXT_COLOR_RED + "Error: " + errMsg + SET_TEXT_COLOR_BLUE); // Print the error
            }
            case NOTIFICATION -> {  // If message is notification type, deserialize into notification class
                String notification = new Gson().fromJson(message, NotificationMessage.class).getMessage();
                System.out.println(notification);   // Print notification to user's terminal
            }
        }
    }

    private void updateGamesList() {
        try {
            ListGamesData gamesList = facade.getGamesList(authToken);
            int i = 0;  // For indexing list
            for (GameData game : gamesList.games()) {
                gameIDs.put(i, game.gameID());  // Pair index with game ID and add to list
                i++;                            // Increment index
            }
        } catch (ResponseException ex) {
            System.out.println("Could not update games list.");
            System.out.println(ex.getMessage());
        }
    }



    // Helper method for printing all games and needed info
    private void listGamesInfo(ListGamesData gamesList) {
        System.out.println("All active chess games:");
        int i = 0;      // Update private list of game IDs
        for (GameData game : gamesList.games()) {
            gameIDs.put(i, game.gameID());  // Update game ID list
            System.out.println(i + ". " + game.gameName());
            if (game.blackUsername() == null) {
                System.out.print("Black team is available. ");
            } else {
                System.out.print("Black team occupied by " + game.blackUsername() + ". ");
            }
            if (game.whiteUsername() == null) {
                System.out.println("White team is available.\n");
            } else {
                System.out.println("White team is occupied by " + game.whiteUsername() + ".\n");
            }
            i++;                            // Increment index
        }
    }

    // Set chess game private member to given parameter
    public void updateGame(ChessGame game) {
        this.chessGame = game;
    }

    private void printHelpScreenLoggedOut() {
        setTextToPromptFormat();
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_BOLD_FAINT + " --> create an account");
        setTextToPromptFormat();
        System.out.println("login <USERNAME> <PASSWORD>" + RESET_TEXT_BOLD_FAINT + " --> login to play chess");
        setTextToPromptFormat();
        System.out.println("quit" + RESET_TEXT_ITALIC + " --> exit application");
    }

    private void printHelpScreenLoggedIn() {
        setTextToPromptFormat();
        System.out.println("create <GAME_NAME>" + RESET_TEXT_BOLD_FAINT + " --> make a new game");
        setTextToPromptFormat();
        System.out.println("list" + RESET_TEXT_BOLD_FAINT + " --> Show list of games");
        setTextToPromptFormat();
        System.out.println("join <ID> [WHITE|BLACK]" + RESET_TEXT_BOLD_FAINT + " --> join game");
        setTextToPromptFormat();
        System.out.println("observe <ID>" + RESET_TEXT_BOLD_FAINT + " --> be a spectator in a game");
        setTextToPromptFormat();
        System.out.println("logout" + RESET_TEXT_BOLD_FAINT + " --> exit when done");
        setTextToPromptFormat();
        System.out.println("help" + RESET_TEXT_BOLD_FAINT + " --> show available options");
    }


    // Calls unicode escape sequences to make command prompt text italicized and green
    private void setTextToPromptFormat() {
        System.out.print(SET_TEXT_BOLD + SET_TEXT_ITALIC + SET_TEXT_COLOR_BLUE);
    }
}
