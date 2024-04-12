package server.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import resException.ResponseException;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

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
        session.getRemote().sendString("Hello world???");

        if (!authDAO.hasAuth(cmd.getAuthString())) {                            // Validate user's auth token
            ErrorMessage errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR);  // Create error message
            errMsg.setMessage("Error: Invalid auth token given.");                          // Write message
            session.getRemote().sendString(new Gson().toJson(errMsg));                      // Send to client
        }
//        else try {
//            if (cmd.getRequestedColor().equals(ChessGame.TeamColor.WHITE)) {    // If user requested white team
////                gameDAO.updateWhiteUsername(cmd.getGameID(), cmd.);
//            }
//
//
//        } catch (Exception ex) {
//
//        }
//        connections.add(authData.username(), session);      // Add the user's name to the list in connections
//        String message = String.format("%s has joined the game.", authData.username());
//        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
//        connections.broadcast(authData.username(), notification);
    }

    private void joinObserver(JoinObserver cmd, Session session) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
    }
}
