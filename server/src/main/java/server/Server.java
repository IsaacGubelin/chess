package server;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import handler.ClearHandler;
import handler.RegisterHandler;
import spark.*;

public class Server {

    // These are the DAOs used by the endpoints of the server.
    // TODO: SQL will go here eventually
    private MemoryUserDAO userDAO = new MemoryUserDAO();
    private MemoryGameDAO gameDAO = new MemoryGameDAO();
    private MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        // FIXME: Take out this init statement once endpoints are available
//        Spark.init();

        // CLEAR APPLICATION
        Spark.delete("/db", (req, res) -> new ClearHandler().clearDatabases(req, res, userDAO, gameDAO, authDAO));

        // REGISTER USER
        Spark.post("/user", (req, res) -> new RegisterHandler().registerHandle(req, res, userDAO, authDAO));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
