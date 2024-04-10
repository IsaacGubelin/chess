package webSocket;


import com.google.gson.Gson;
import resException.ResponseException;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import javax.websocket.*;
import java.io.IOException;
import java.net.*;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

// TODO: Find out what communication happens between this facade, the client, the server facade, and the websocket handler

public class WebSocketFacade extends Endpoint {

    Session session;
    ServiceMessageHandler messageHandler;

    // Print notification message for players/observers joining or leaving games
    private void clientNotify(NotificationMessage message) {
        System.out.println(SET_TEXT_COLOR_RED + message.getMessage() + SET_TEXT_COLOR_BLUE);
    }

    // Print error message
    private void clientErrorMessage(ErrorMessage message) {
        System.out.println("Error: " + message.getMessage());
    }

    public void wsLoadGame(LoadGameMessage message) {

    }


    public WebSocketFacade(String url, ServiceMessageHandler msgHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.messageHandler = msgHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() { // DO NOT replace with lambda
                @Override
                public void onMessage(String message) {
                    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
                    switch (msg.getServerMessageType()) {   // Choose what to do with message based on type
                        case LOAD_GAME:

                            break;

                        case ERROR:
                            ErrorMessage errMsg = new Gson().fromJson(message, ErrorMessage.class);
                            break;

                        case NOTIFICATION:  // If message is notification type, deserialize into notification class
                            NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                            clientNotify(notification);
                            break;
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }


    }


    //Endpoint requires this method, but you don't have to do anything
    @Override
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        // FIXME: Add print statement
    }

}
