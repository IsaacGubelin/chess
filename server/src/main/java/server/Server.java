package server;

import dataAccess.*;
import handler.ClearHandler;
import handler.GameHandler;
import handler.LoginOutHandler;
import handler.RegisterHandler;
import server.webSocket.WebSocketHandler;
import spark.*;

public class Server {

    /**
     *  - Three SQL DAOs for game, auth, and user
     */

    private WebSocketHandler socketHandler = new WebSocketHandler();

    // Collection of SQL DAOs. These retain data even when server is offline.
    public SQLUserDAO sqlUserDAO = new SQLUserDAO();
    public SQLGameDAO sqlGameDAO = new SQLGameDAO();
    public SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();


    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", socketHandler);

        // CLEAR APPLICATION
        Spark.delete("/db", (req, res) -> new ClearHandler().clearDatabases(req, res, sqlGameDAO, sqlUserDAO, sqlAuthDAO));

        // REGISTER USER
        Spark.post("/user", (req, res) -> new RegisterHandler().registerHandle(req, res, sqlUserDAO, sqlAuthDAO));

        // LOGIN USER
        Spark.post("/session", (req, res) -> new LoginOutHandler().loginHandle(req, res, sqlUserDAO, sqlAuthDAO) );

        // LOGOUT USER
        Spark.delete("/session", (req, res) -> new LoginOutHandler().logoutHandle(req, res, sqlAuthDAO));

        // CREATE GAME
        Spark.post("/game", (req, res) -> new GameHandler().createGameHandle(req, res, sqlAuthDAO, sqlGameDAO));

        // LIST GAMES
        Spark.get("/game", (req, res) -> new GameHandler().listGamesHandle(req, res, sqlAuthDAO, sqlGameDAO));

        // JOIN GAME
        Spark.put("/game", (req, res) -> new GameHandler().joinGameHandle(req, res, sqlAuthDAO, sqlGameDAO));


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}