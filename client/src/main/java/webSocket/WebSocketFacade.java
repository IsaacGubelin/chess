package webSocket;


import org.glassfish.grizzly.http.server.Session;
import org.glassfish.tyrus.client.ClientManager;
import resException.ResponseException;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }


    }


    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

//public WebSocketFacade(String url, ServiceMessageHandler msgHandler) throws ResponseException {
//    try {
//        url = url.replace("http", "ws");
//        URI socketURI = new URI(url + "/connect");
//        this.messageHandler = msgHandler;
//
//        WebSocketContainer container = ClientManager.createClient();
//        container.connectToServer(this, socketURI);
//
//    } catch (DeploymentException | IOException | URISyntaxException ex) {
//        throw new ResponseException(500, ex.getMessage());
//    }
//}
//
//    // Method to handle incoming messages
//    public void onMessage(String message) {
//        // Your message handling logic here
//        // Example: messageHandler.handleMessage(message);
//    }
//
//
}
