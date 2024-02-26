package server;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import handler.ClearHandler;
import handler.GameHandler;
import handler.LoginOutHandler;
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

        // CLEAR APPLICATION
        Spark.delete("/db", (req, res) -> new ClearHandler().clearDatabases(req, res, userDAO, gameDAO, authDAO));

        // REGISTER USER
        Spark.post("/user", (req, res) -> new RegisterHandler().registerHandle(req, res, userDAO, authDAO));

        // LOGIN USER
        Spark.post("/session", (req, res) -> new LoginOutHandler().loginHandle(req, res, userDAO, authDAO) );

        // LOGOUT USER
        Spark.delete("/session", (req, res) -> new LoginOutHandler().logoutHandle(req, res, authDAO));

        Spark.post("/game", (req, res) -> new GameHandler().createGameHandle(req, res, authDAO, gameDAO));

        // LIST GAMES //FIXME: Change format of games list to match requested json
        Spark.get("/game", (req, res) -> new GameHandler().listGamesHandle(req, res, authDAO, gameDAO));

        //TODO:
        // Make remaining endpoints for other functions


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
