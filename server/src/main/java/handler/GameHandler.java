package handler;

import com.google.gson.Gson;
import config.Config;
import dataAccess.*;
import model.*;
import service.GameService;
import spark.Request;
import spark.Response;

public class GameHandler {

    Gson gs = new Gson();   // Gson object for serialization/deserialization

    public Object listGamesHandle(Request req, Response res, MemoryAuthDAO aDAO, MemoryGameDAO gDAO) {

        String authToken = req.headers(Config.LOGOUT_REQ_HEADER);   // Get authToken for verification

        // Verify the authToken for authorization
        if (!aDAO.hasAuth(authToken)) { // If authToken isn't in database, return error status message
            res.status(401);
            return gs.toJson(new MessageData("Error: unauthorized"));
        }
        // If authorized, return games list
        res.status(200);
        return gs.toJson(new ListGamesData(GameService.getGames(gDAO)));
    }

    public Object createGameHandle(Request req, Response res, MemoryAuthDAO aDAO, MemoryGameDAO gDAO) {

        String authToken = req.headers(Config.LOGOUT_REQ_HEADER);   // Get authToken for verification

        // Verify the authToken for authorization
        if (!aDAO.hasAuth(authToken)) { // If authToken isn't in database, return error status message
            res.status(401);
            return gs.toJson(new MessageData("Error: unauthorized"));
        }
        // Gather needed information from json request body
        GameRequestData name = gs.fromJson(req.body(), GameRequestData.class);

        // If game name parameter is null, this is a bad request.
        if (name.gameName() == null) {
            res.status(400);
            return gs.toJson(new MessageData("Error: bad request"));
        }

        try {
            int gameID = GameService.createGame(name.gameName(), gDAO);
            res.status(200);
            return gs.toJson(new GameIDData(gameID));
        } catch (DataAccessException dataEx) {
            res.status(403);
            return gs.toJson(new MessageData("Error: No more available games"));
        }
    }

    public Object joinGameHandle(Request req, Response res, MemoryAuthDAO aDAO, MemoryGameDAO gDAO) {

        String authToken = req.headers(Config.LOGOUT_REQ_HEADER);   // Get authToken for verification

        // Verify the authToken for authorization
        if (!aDAO.hasAuth(authToken)) { // If authToken isn't in database, return error status message
            res.status(401);
            return gs.toJson(new MessageData("Error: unauthorized"));
        }
        // Gather the required information from json request body
        GameRequestData joinGameData = gs.fromJson(req.body(), GameRequestData.class);

        try {
            GameService.joinGame(joinGameData.gameID(), joinGameData.playerColor(), authToken, gDAO, aDAO);
            res.status(200);
            return "{}";    // No return body, give empty json
        } catch (BadRequestException badEx) {   // If gameID didn't exist or color was invalid
            res.status(400); // Set error code for bad request
            return gs.toJson(new MessageData("Error: bad request"));
        } catch (AlreadyTakenException alrEx) { // If requested team color is already claimed
            res.status(403); // Set error code for already-taken exception
            return gs.toJson(new MessageData("Error: already taken"));
        }
    }
}
