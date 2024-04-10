package webSocket;


import com.google.gson.Gson;
import resException.ResponseException;
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

    private void clientNotify(NotificationMessage message) {
        System.out.println(SET_TEXT_COLOR_RED + message.getMessage() + SET_TEXT_COLOR_BLUE);
    }

    private void wsLoadGame(LoadGameMessage message) {

    }


    public WebSocketFacade(String url, ServiceMessageHandler msgHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.messageHandler = msgHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
                    switch (msg.getServerMessageType()) {
                        case LOAD_GAME:

                            break;

                        case ERROR:
                            break;

                        case NOTIFICATION:  // If message is notification type, deserialize into notification class
                            NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                            clientNotify(notification);
                            break;
                    }
//                    ServiceMessageHandler.notify(notification);
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
