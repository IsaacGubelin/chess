package server.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import exception.AlreadyTakenException;
import exception.DataAccessException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import resException.ResponseException;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.sql.SQLException;

//TODO:
// Add a mapping data structure that holds key-value pairs:
// KEY: gameID, VALUE: List of connections/sessions

@WebSocket
public class WebSocketHandler {

    // A container to keep track of connections related to single game (players and observers)
    private final ConnectionManager connections = new ConnectionManager();

    private final SQLAuthDAO authDAO = new SQLAuthDAO();    // Used for verifying auth tokens
    private final SQLGameDAO gameDAO = new SQLGameDAO();    // For updating chess games
    private final SQLUserDAO userDAO = new SQLUserDAO();    // For retrieving usernames //FIXME: possible removal

    // ServerMessage types for replying to client requests
    private final ServerMessage.ServerMessageType LOAD = ServerMessage.ServerMessageType.LOAD_GAME;
    private final ServerMessage.ServerMessageType ERR = ServerMessage.ServerMessageType.ERROR;
    private final ServerMessage.ServerMessageType NOTIFY = ServerMessage.ServerMessageType.NOTIFICATION;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, ResponseException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        // TODO:
        //  Send to appropriate helper method
        switch (action.getCommandType()) {
            case JOIN_PLAYER:
                JoinPlayer joinPlayercmd = new Gson().fromJson(message, JoinPlayer.class);
                joinPlayer(joinPlayercmd, session);
                break;

            case JOIN_OBSERVER:
                JoinObserver joinObservercmd = new Gson().fromJson(message, JoinObserver.class);
                joinObserver(joinObservercmd, session);
                break;

            case LEAVE:
                break;

            case MAKE_MOVE:
                break;

            case RESIGN:
                break;

        }
    }


    private void joinPlayer(JoinPlayer cmd, Session session) throws IOException, ResponseException {

        // TODO: This part is working! Now you just need to do some SQL finessing and stuff. WIN THIS DAY!

        // Validate user's auth token and existence of game ID
        if (!authDAO.hasAuth(cmd.getAuthString())) {                            // Check the given auth token
            sendErrorMessage("Error: Invalid auth token given.", session); // Send error message if invalid
        } else if (!gameDAO.hasGame(cmd.getGameID())) {                         // Check requested game ID
            sendErrorMessage("Error: Invalid game ID.", session);          // Send error if game ID doesn't exist
        } else try {                                                              // Otherwise, proceed with join
            String username = authDAO.getAuth(cmd.getAuthString()).username();  // Retrieve username
            if (cmd.getRequestedColor().equals(ChessGame.TeamColor.WHITE)) {    // If user requested white team
                gameDAO.updateWhiteUsername(cmd.getGameID(), username);
            } else if (cmd.getRequestedColor().equals(ChessGame.TeamColor.BLACK)) { // If black team request
                gameDAO.updateBlackUsername(cmd.getGameID(), username);
            }
            // TODO: Retrieve game from SQL, make LoadGame message, and send back to client
            sendLoadGameMessage(cmd.getGameID(), session);  // Send a successful load game message to client

        } catch (DataAccessException | SQLException ex) {                       // If an exception is thrown
            sendErrorMessage("Error in accessing SQL database", session);   // SQL error message
        } catch (AlreadyTakenException alrEx) {
            sendErrorMessage("Team already used.", session);                // Send error message if team is taken
        }

//        connections.add(authData.username(), session);      // Add the user's name to the list in connections
//        String message = String.format("%s has joined the game.", authData.username());
//        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
//        connections.broadcast(authData.username(), notification);
    }

    /**
     * Helper method for creating, writing, and sending error messages to the client
     * @param msg
     * @param session
     */
    private void sendErrorMessage(String msg, Session session) throws IOException {
        Error errMsg = new Error(ServerMessage.ServerMessageType.ERROR);  // Create error message
        errMsg.setMessage(msg);                                                         // Write message
        session.getRemote().sendString(new Gson().toJson(errMsg));                      // Send to client
    }

    /**
     * Helper method for sending a load_game message to client
     * @param gameID
     * @param session
     * @throws IOException
     * @throws SQLException
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
