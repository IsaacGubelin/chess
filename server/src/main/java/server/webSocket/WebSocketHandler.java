package server.webSocket;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import model.AuthData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.glassfish.grizzly.http.server.Session;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(action.getAuthString(), session);     // FIXME: Add function call?
            case JOIN_OBSERVER -> joinObserver(action.getAuthString(), session);
        }
    }


    private void joinPlayer(String visitorName, Session session) throws IOException {
        AuthData authData = new Gson().fromJson(visitorName, AuthData.class);  // Convert json back to auth record
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
    }

    private void joinObserver(String visitorName, Session session) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
    }
}
