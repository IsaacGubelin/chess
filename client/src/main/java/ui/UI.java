package ui;
import chess.ChessGame;
import facade.ServerFacade;
import model.*;
import resException.ResponseException;

import java.util.HashMap;
import java.util.Scanner;
import static ui.EscapeSequences.*;


public class UI {

    private final String LOGGED_OUT = "[LOGGED_OUT]";   // Used in pre-login prompts
    private final String LOGGED_IN = "[LOGGED_IN]";     // Used in post-login prompts
    private String status;                              // State of client
    private String authToken;                           // Keeps track of the user's authToken
    private final String url;
    private final ServerFacade facade;                  // Access the facade methods
    private HashMap<Integer, Integer> gameIDs;                  // Keeps track of listed games

    private int currentGameIndex;

    // Constructor for Client UI object
    public UI(String url) {
        facade = new ServerFacade(url); // Initialize server facade with given url
        this.url = url;                 // Store url
        initClientUI();                 // Initialize all variables
    }

    private void initClientUI() {
        gameIDs = new HashMap<>();      // Initialize game ID container
        status = LOGGED_OUT;            // Status starts in logged out state
        authToken = "";                 // No token at startup
        currentGameIndex = -1;             // Will be updated to positive value when user joins/observes game
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
                        System.out.println("Your auth token is " + authData.authToken());
                        authToken = authData.authToken();        // Store token
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
                        System.out.println("Your auth token is " + authData.authToken());
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
                            System.out.println("Observing game " + currentGameIndex);
                        }
                    } catch (NumberFormatException numEx) {
                        System.out.println("Second argument not a number. Must use integer.");
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


    // Gets called when user successfully uses register command
    private void registerPrompt(String name, String password, String email) {

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
