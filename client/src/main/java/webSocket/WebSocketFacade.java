package webSocket;


import com.google.gson.Gson;
import resException.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import javax.websocket.*;
import java.io.IOException;
import java.net.*;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class WebSocketFacade extends Endpoint {

    Session session;
    ServiceMessageHandler messageHandler;

    // TODO: Put case statements to account for each type of server message
    private void notify(ServerMessage message) {

    }


    public WebSocketFacade(String url, ServiceMessageHandler msgHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.messageHandler = msgHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = (Session) container.connectToServer(this, socketURI);
            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
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
