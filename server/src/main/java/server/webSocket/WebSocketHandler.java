package server.webSocket;

import com.google.gson.Gson;
import model.AuthData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    // A container to keep track of connections related to single game (players and observers)
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(action.getAuthString(), session);     // FIXME: Add function call?
            case JOIN_OBSERVER -> joinObserver(action.getAuthString(), session);
        }
    }


    private void joinPlayer(String visitorInfo, Session session) throws IOException {
        AuthData authData = new Gson().fromJson(visitorInfo, AuthData.class);  // Convert json back to auth record
        connections.add(authData.username(), session);      // Add the user's name to the list in connections
        var message = String.format("%s has joined the game.", authData.username());
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(authData.username(), notification);
    }

    private void joinObserver(String visitorName, Session session) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
    }
}
