package handler;

import com.google.gson.Gson;
import config.Config;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.*;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class GameHandler {

    public Object listGamesHandle(Request req, Response res, MemoryAuthDAO aDAO, MemoryGameDAO gDAO) {

        String authToken = req.headers(Config.LOGOUT_REQ_HEADER);   // Get authToken for verification

        // Verify the authToken for authorization
        if (!aDAO.hasAuth(authToken)) { // If authToken isn't in database, return error status message
            res.status(401);
            return new Gson().toJson(new MessageData("Error: unauthorized"));
        }
        // If authorized, return games list
        res.status(200);
        return new Gson().toJson(new ListGamesData(GameService.getGames(gDAO)));
    }

    public Object createGameHandle(Request req, Response res, MemoryAuthDAO aDAO, MemoryGameDAO gDAO) {

        String authToken = req.headers(Config.LOGOUT_REQ_HEADER);   // Get authToken for verification

        // Verify the authToken for authorization
        if (!aDAO.hasAuth(authToken)) { // If authToken isn't in database, return error status message
            res.status(401);
            return new Gson().toJson(new MessageData("Error: unauthorized"));
        }
        GameNameData name = new Gson().fromJson(req.body(), GameNameData.class);
        try {
            int gameID = GameService.createGame(name.gameName(), gDAO);
            res.status(200);
            return new Gson().toJson(new GameIDData(gameID));
        } catch (DataAccessException dataEx) {
            res.status(403);
            return new Gson().toJson(new MessageData("Error: No more available games"));
        }
    }

    public Object joinGameHandle(Request req, Response res, MemoryAuthDAO aDAO, MemoryGameDAO gDAO) {

        String authToken = req.headers(Config.LOGOUT_REQ_HEADER);   // Get authToken for verification

        // Verify the authToken for authorization
        if (!aDAO.hasAuth(authToken)) { // If authToken isn't in database, return error status message
            res.status(401);
            return new Gson().toJson(new MessageData("Error: unauthorized"));
        }

        // fIXME:
        return null;
    }

}
