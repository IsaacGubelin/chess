package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

// TODO:
//  CHANGE THE HASHMAP to use integer keys for game IDs

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session) {
        var connection = new Connection(visitorName, session);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    // Send a message out to many clients. Useful for observer/player joining notifications.
    public void broadcast(String excludeVisitorName, ServerMessage msg) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {                        // Go through all connections
            if (c.session.isOpen()) {                               // Check if their session is open
                if (!c.visitorName.equals(excludeVisitorName)) {    // Exclude the user who is notifying others
                    c.send(msg.toString());                         // Send the notification
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }

}
