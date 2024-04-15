package server.webSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import exception.DataAccessException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;
import server.webSocket.Connection;

import java.io.IOException;
import java.sql.SQLException;

@WebSocket
public class WebSocketHandler {

    // A container to keep track of connections related to single game (players and observers)
    private final ConnectionManager connManager = new ConnectionManager();

    private final SQLAuthDAO authDAO = new SQLAuthDAO();    // Used for verifying auth tokens
    private final SQLGameDAO gameDAO = new SQLGameDAO();    // For updating chess games

    // ServerMessage types for replying to client requests
    private final ServerMessage.ServerMessageType LOAD = ServerMessage.ServerMessageType.LOAD_GAME;
    private final ServerMessage.ServerMessageType ERR = ServerMessage.ServerMessageType.ERROR;
    private final ServerMessage.ServerMessageType NOTIFY = ServerMessage.ServerMessageType.NOTIFICATION;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        // TODO:
        //  Send to appropriate helper method
        switch (action.getCommandType()) {
            case JOIN_PLAYER:
                JoinPlayer joinPlayerCmd = new Gson().fromJson(message, JoinPlayer.class); // Make JoinPlayer format
                joinPlayer(joinPlayerCmd, session);
                break;

            case JOIN_OBSERVER:
                JoinObserver joinObserverCmd = new Gson().fromJson(message, JoinObserver.class); // JoinObserver format
                joinObserver(joinObserverCmd, session);
                break;

            case LEAVE:
                Leave lvCmd = new Gson().fromJson(message, Leave.class);    // Make into Leave command format
                removeUserFromGame(lvCmd, session);                         // User exits game
                break;

            case MAKE_MOVE:
                MakeMove moveCmd = new Gson().fromJson(message, MakeMove.class);    // MakeMove format
                playerMove(moveCmd, session);                                       // Attempt to make move
                break;

            case RESIGN:
                break;

        }
    }


    private void joinPlayer(JoinPlayer cmd, Session session) throws IOException {

        // Validate user's auth token and existence of game ID
        if (!authDAO.hasAuth(cmd.getAuthString())) {                            // Check the given auth token
            sendErrorMessage("Error: Invalid auth token given.", session); // Send error message if invalid
        } else if (!gameDAO.hasGame(cmd.getGameID())) {                         // Check requested game ID
            sendErrorMessage("Error: Invalid game ID.", session);          // Send error if game ID doesn't exist
        }
        else try {                                                              // Otherwise, proceed with join
            String joinName = authDAO.getAuth(cmd.getAuthString()).username();  // Username from requester
            String foundName = gameDAO.getUsername(cmd.getGameID(), cmd.getRequestedColor()); // Updated username
            // Join attempt is a failure if the username in the requested game is null or doesn't match
            if (foundName == null || !foundName.equals(joinName)) {
                sendErrorMessage("Error: user failed to join game.", session);  // Send an error message
            } else {
                sendLoadGameMessage(cmd.getGameID(), session);  // If joined, send a load game message to client
                connManager.add(cmd.getGameID(), cmd.getAuthString(), session);     // Add session to list of sessions
                sendBroadcastJoinPlayer(cmd);                                       // Broadcast a notification
            }

        } catch (DataAccessException | SQLException ex) {                       // If an exception is thrown
            sendErrorMessage("Error: trouble accessing SQL database", session);   // SQL error message
        }
    }

    private void removeUserFromGame(Leave leaveCmd, Session session) throws IOException {
        try {
            String leaveName = authDAO.getAuth(leaveCmd.getAuthString()).username();    // Get username from token
            int id = leaveCmd.getGameID();                                              // Get game ID
            if (!gameDAO.hasGame(leaveCmd.getGameID())) {                         // Check requested game ID
                sendErrorMessage("Error: Invalid game ID.", session);       // Send error if game ID doesn't exist
            }   // Check if user requesting to leave is on black team
            else if (gameDAO.getUsername(id, ChessGame.TeamColor.BLACK).equals(leaveName)) {
                gameDAO.removePlayer(id, ChessGame.TeamColor.BLACK);
            }   // else, check if user is on the white team
            else if (gameDAO.getUsername(id, ChessGame.TeamColor.WHITE).equals(leaveName)) {
                gameDAO.removePlayer(id, ChessGame.TeamColor.WHITE);
            }
            connManager.removeConnection(leaveCmd.getGameID(), leaveCmd.getAuthString()); // Take user off broadcast list

            sendBroadcastPlayerLeft(leaveCmd);                                  // Let everyone know that user left
        } catch (DataAccessException | SQLException ex) {
            sendErrorMessage("Error: Could not leave game.", session);  // Send error message if leave fails
        }
    }

    /**
     * This method is called when the websocket detects a JoinObserver command.
     * @param cmd contains game ID of game observer wishes to watch
     * @param session connection to observer
     * @throws IOException May be thrown from notification broadcast
     */
    private void joinObserver(JoinObserver cmd, Session session) throws IOException {
        // Validate user's auth token and existence of game ID
        if (!authDAO.hasAuth(cmd.getAuthString())) {                            // Check the given auth token
            sendErrorMessage("Error: Invalid auth token given.", session); // Send error message if invalid
        } else if (!gameDAO.hasGame(cmd.getGameID())) {                         // Check requested game ID
            sendErrorMessage("Error: Invalid game ID.", session);          // Send error if game ID doesn't exist
        } else try {                                                              // Otherwise, add observer

            sendLoadGameMessage(cmd.getGameID(), session);  // Send a load game message to client
            connManager.add(cmd.getGameID(), cmd.getAuthString(), session);     // Add session to list of sessions
            sendBroadcastJoinObserver(cmd);                                     // Broadcast a notification

        } catch (DataAccessException | SQLException ex) {                       // If an exception is thrown
            sendErrorMessage("Error: trouble accessing SQL database", session);   // SQL error message
        }
    }

    /**
     * Handles a MakeMove command. Verifies that the move is legal and throws an error otherwise.
     * @param moveCmd contains the needed game ID and chess move information
     * @param session connection to client who sent the command
     * @throws IOException Possible exception
     */
    private void playerMove(MakeMove moveCmd, Session session) throws IOException {
        if (!authDAO.hasAuth(moveCmd.getAuthString())) {
            sendErrorMessage("Error: Invalid authToken.", session);
        } else if (!gameDAO.hasGame(moveCmd.getGameID())) {
            sendErrorMessage("Error: game ID does not exist in database.", session);
        } else try {
            gameDAO.updateGameMakeMove(moveCmd.getGameID(), moveCmd.getMove()); // Make the move
            SendBroadcastLoadGame(moveCmd);         // Load new board for everyone, including player who made the move
        } catch (SQLException | DataAccessException ex) {
            sendErrorMessage("Error: trouble accessing SQL database.", session);
        } catch (InvalidMoveException invEx) {
            sendErrorMessage("Error: invalid chess move.", session);
        }
    }

    /**
     * Helper method for creating, writing, and sending a notification to all clients when a new user joins their game
     * @param cmd This is a JoinPlayer UserGameCommand.
     * @throws DataAccessException Thrown if authToken doesn't exist
     */
    void sendBroadcastJoinPlayer(JoinPlayer cmd) throws DataAccessException, IOException {
        String broadcastMsg = authDAO.getAuth(cmd.getAuthString()).username();  // Start message with user's name
        broadcastMsg += " has joined the game.";
        Notification notification = new Notification(NOTIFY, broadcastMsg);
        connManager.broadcast(cmd.getGameID(), cmd.getAuthString(), notification);
    }

    void sendBroadcastJoinObserver(JoinObserver obsCmd) throws DataAccessException, IOException {
        String broacastMsg = authDAO.getAuth(obsCmd.getAuthString()).username(); // Start message with observer's name
        broacastMsg += " is watching the game.";
        Notification notification = new Notification(NOTIFY, broacastMsg);  // Create new notification
        connManager.broadcast(obsCmd.getGameID(), obsCmd.getAuthString(), notification); // Send to users in same game
    }

    void sendBroadcastPlayerLeft(Leave lvCmd) throws DataAccessException, IOException {
        String broadcastMsg = authDAO.getAuth(lvCmd.getAuthString()).username();    // Find name of user that left
        broadcastMsg += " has left the game.";
        Notification notification = new Notification(NOTIFY, broadcastMsg);         // Make a new notification
        connManager.broadcast(lvCmd.getGameID(), lvCmd.getAuthString(), notification);  // Send to users in same game
    }

    void SendBroadcastLoadGame(MakeMove moveCmd) throws IOException, SQLException, DataAccessException {
        int gameID = moveCmd.getGameID();                                       // Retrieve ID for easy reference
        for (Connection conn : connManager.gameConnections.get(gameID)) {
            sendLoadGameMessage(gameID, conn.session);                          // Update everyone's boards
        }
        String broadcastMsg = authDAO.getAuth(moveCmd.getAuthString()).username();  // Start update message with name
        broadcastMsg += " has made a move: " + moveCmd.getMove().toString();        // Chess move information
        Notification notification = new Notification(NOTIFY, broadcastMsg);         // Make a notification
        connManager.broadcast(gameID, moveCmd.getAuthString(), notification);   // Send notification about new move
    }

    /**
     * Helper method for creating, writing, and sending error messages to the client
     * @param msg The error message that will be displayed on the client's terminal
     * @param session The session for the client
     * @throws IOException IO error
     */
    private void sendErrorMessage(String msg, Session session) throws IOException {
        Error errMsg = new Error(ERR);                                                  // Create error message
        errMsg.setMessage(msg);                                                         // Write message
        session.getRemote().sendString(new Gson().toJson(errMsg));                      // Send to client
    }

    /**
     * Helper method for sending a load_game message to client
     * @param gameID This is used to retrieve the correct game from the SQL database
     * @param session   The client's websocket session
     * @throws IOException Thrown for IO error
     * @throws SQLException May be thrown if database access doesn't work
     */
    private void sendLoadGameMessage(int gameID, Session session) throws IOException, SQLException{
        ServerMessage loadMsg = new LoadGame(LOAD, gameDAO.getChessGameFromDatabase(gameID));
        session.getRemote().sendString(new Gson().toJson(loadMsg));
    }


}
