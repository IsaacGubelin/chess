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
     * This DAO collection object contains six DAOs:
     * <p>
     *     - Three memory DAOs for game, auth, and user
     * </p> <p>
     *     - Three SQL DAOs for game, auth, and user
     * </p>
     */
    private DatabaseDAOCollection dataObjects = new DatabaseDAOCollection();
    private WebSocketHandler socketHandler = new WebSocketHandler();


    // Collection of regular memory DAOs. These erase when the server stops.
    public MemoryUserDAO memUserDAO = new MemoryUserDAO();
    public MemoryGameDAO memGameDAO = new MemoryGameDAO();
    public MemoryAuthDAO memAuthDAO = new MemoryAuthDAO();

    // Collection of SQL DAOs. These retain data even when server is offline.
    public SQLUserDAO sqlUserDAO = new SQLUserDAO();
    public SQLGameDAO sqlGameDAO = new SQLGameDAO();
    public SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();


    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", socketHandler);

        // CLEAR APPLICATION
        Spark.delete("/db", (req, res) -> new ClearHandler().clearDatabases(req, res, dataObjects.sqlGameDAO, dataObjects.sqlUserDAO, dataObjects.sqlAuthDAO));

        // REGISTER USER
        Spark.post("/user", (req, res) -> new RegisterHandler().registerHandle(req, res, dataObjects.sqlUserDAO, dataObjects.sqlAuthDAO));

        // LOGIN USER
        Spark.post("/session", (req, res) -> new LoginOutHandler().loginHandle(req, res, dataObjects.sqlUserDAO, dataObjects.sqlAuthDAO) );

        // LOGOUT USER
        Spark.delete("/session", (req, res) -> new LoginOutHandler().logoutHandle(req, res, dataObjects.sqlAuthDAO));

        // CREATE GAME
        Spark.post("/game", (req, res) -> new GameHandler().createGameHandle(req, res, dataObjects.sqlAuthDAO, dataObjects.sqlGameDAO));

        // LIST GAMES
        Spark.get("/game", (req, res) -> new GameHandler().listGamesHandle(req, res, dataObjects.sqlAuthDAO, dataObjects.sqlGameDAO));

        // JOIN GAME
        Spark.put("/game", (req, res) -> new GameHandler().joinGameHandle(req, res, dataObjects.sqlAuthDAO, dataObjects.sqlGameDAO));


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}