package server;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import spark.*;

public class Server {

    // These are the DAOs used by the endpoints of the server.
    private MemoryAuthDAO memAuthDAO = new MemoryAuthDAO();
    private MemoryUserDAO memUserDAO = new MemoryUserDAO();
    private MemoryGameDAO memGameDAO = new MemoryGameDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        // Take out when other methods are available
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
