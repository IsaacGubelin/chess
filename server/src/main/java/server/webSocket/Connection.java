package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

// A collection of these will be held in a data structure for easy communication of notifications.
public class Connection {
    public String visitorName;
    public Session session;

    public Connection(String visitorName, Session session) {
        this.visitorName = visitorName;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
