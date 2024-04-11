package webSocket;


import chess.ChessGame;
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

public class WebSocketFacade extends Endpoint {

    Session session;
    ServiceMessageHandler messageHandler;


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
                    msgHandler.notify(message);
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
        System.out.println("onOpen reached within client websocket.");
    }

}
