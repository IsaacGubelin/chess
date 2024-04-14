package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;
import resException.ResponseException;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
public class ConnectionManager {
    public final ConcurrentHashMap<Integer, HashSet<Connection>> gameConnections = new ConcurrentHashMap<>();

    /**
     * This adds a session to a group of sessions associated with a single chess game.
     * <p>
     * As users leave or join a chess game, notifications are sent to all sessions connected to that game's ID.
     * @param gameID
     * @param authToken
     * @param session
     * @throws ResponseException
     */
    public void add(int gameID, String authToken, Session session) {
        Connection connection = new Connection(authToken, session);         // Make a Connection object
        if (gameConnections.containsKey(gameID)) {                             // If the game already exists
            gameConnections.get(gameID).add(connection);                    // Add connection for new observer/player
        } else {
            HashSet<Connection> connections = new HashSet<>();              // Otherwise, make a new entry
            connections.add(connection);                                    // Add connection to list
            gameConnections.put(gameID, connections);                       // Insert into map
        }
    }

    /**
     * Removes the given auth token from the session collection held by the given gameID.
     * @param gameID
     * @param authToken
     */
    public void remove(int gameID, String authToken) {
        gameConnections.get(gameID).remove(authToken);
    }

    // Send a message out to many clients. Useful for observer/player joining notifications.
    public void broadcast(int gameID, String excludeAuthToken, Notification msg) throws IOException {
        HashSet<Connection> notifyList = gameConnections.get(gameID);   // Get all sessions to notify
        ArrayList<Connection> removeList = new ArrayList<>();           // Add closed connections to this list
        for (Connection c : notifyList) {                                      // Go through all connections
            if (c.session.isOpen()) {                                   // Check if their session is open
                if (!c.authToken.equals(excludeAuthToken)) {            // Exclude the user who is notifying others
                    c.send(msg);                             // Send the notification
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            gameConnections.get(gameID).remove(c);
        }
    }

}
