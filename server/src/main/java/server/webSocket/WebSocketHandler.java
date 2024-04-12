package server.webSocket;

import com.google.gson.Gson;
import dataAccess.SQLAuthDAO;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import resException.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    // A container to keep track of connections related to single game (players and observers)
    private final ConnectionManager connections = new ConnectionManager();

    private final SQLAuthDAO authDAO = new SQLAuthDAO();    // Used for verifying auth tokens

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, ResponseException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        // TODO:
        //  Determine type
        //  Do ANOTHER from-json conversion to specific child class
        //  Send to appropriate helper method
        switch (action.getCommandType()) {
            case JOIN_PLAYER:
                JoinPlayerCommand joinPlayercmd = new Gson().fromJson(message, JoinPlayerCommand.class);
                joinPlayer(joinPlayercmd, session);
                break;
            case JOIN_OBSERVER:
                JoinObserverCommand joinObservercmd = new Gson().fromJson(message, JoinObserverCommand.class);
                joinObserver(joinObservercmd, session);
                break;
        }
    }


    private void joinPlayer(JoinPlayerCommand cmd, Session session) throws IOException, ResponseException {

//        if (!authDAO.hasAuth(cmd.getAuthString())) {
//            throw new ResponseException(500, "Invalid authToken");
//        }
        // TODO: This part is working! Now you just need to do some SQL finessing and stuff. WIN THIS DAY!
        session.getRemote().sendString("Hello world???");
//        connections.add(authData.username(), session);      // Add the user's name to the list in connections
//        String message = String.format("%s has joined the game.", authData.username());
//        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
//        connections.broadcast(authData.username(), notification);
    }

    private void joinObserver(JoinObserverCommand cmd, Session session) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
    }
}
