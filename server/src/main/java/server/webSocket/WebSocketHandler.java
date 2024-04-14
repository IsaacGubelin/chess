package server.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import exception.AlreadyTakenException;
import exception.DataAccessException;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.UserGameCommand;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

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
                break;

            case RESIGN:
                break;

        }
    }


    private void joinPlayer(JoinPlayer cmd, Session session) throws IOException {

        // TODO:  WIN THIS DAY!

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
                System.out.println("name was NOT null");
                sendLoadGameMessage(cmd.getGameID(), session);  // If joined, send a load game message to client
                connManager.add(cmd.getGameID(), cmd.getAuthString(), session);     // Add session to list of sessions
                sendBroadcastJoinPlayer(cmd);                                       // Broadcast a notification
            }

        } catch (DataAccessException | SQLException ex) {                       // If an exception is thrown
            sendErrorMessage("Error: trouble accessing SQL database", session);   // SQL error message
        } /*catch (AlreadyTakenException alrEx) {
            sendErrorMessage("Team already used.", session);                // Send error message if team is taken
        } */
    }

    private void removeUserFromGame(Leave leaveCmd, Session session) throws IOException {
        System.out.println("In removeUserFromGame in websocket handler");
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
            connManager.remove(leaveCmd.getGameID(), leaveCmd.getAuthString()); // Take user off broadcast list
            sendBroadcastPlayerLeft(leaveCmd);                                  // Let everyone know that user left
        } catch (DataAccessException | SQLException ex) {
            sendErrorMessage("Error: Could not leave game.", session);  // Send error message if leave fails
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

    void sendBroadcastPlayerLeft(Leave lvCmd) throws DataAccessException, IOException {
        String broadcastMsg = authDAO.getAuth(lvCmd.getAuthString()).username();    // Find name of user that left
        broadcastMsg += " has left the game.";
        Notification notification = new Notification(NOTIFY, broadcastMsg);         // Make a new notification
        connManager.broadcast(lvCmd.getGameID(), lvCmd.getAuthString(), notification);  // Send to users in same game
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

    private void joinObserver(JoinObserver cmd, Session session) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
    }
}
