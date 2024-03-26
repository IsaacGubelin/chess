package handler;

import com.google.gson.Gson;
import config.ConfigConsts;
import dataAccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.DataAccessException;
import model.*;
import service.GameService;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class GameHandler {

    Gson gs = new Gson();   // Gson object for serialization/deserialization

    public Object listGamesHandle(Request req, Response res, AuthDAO aDao, GameDAO gDao) {

        String authToken = req.headers(ConfigConsts.LOGOUT_REQ_HEADER);   // Get authToken for verification

        // Verify the authToken for authorization
        if (!aDao.hasAuth(authToken)) { // If authToken isn't in database, return error status message
            res.status(401);
            return gs.toJson(new MessageData("Error: unauthorized"));
        }
        // If authorized, return games list
        try {
            ListGamesData gamesList = new ListGamesData(GameService.getGames(gDao));
            res.status(200);
            return gs.toJson(gamesList);
        } catch (SQLException e) {
            res.status(500);
            return gs.toJson(new MessageData("Error: SQL exception."));
        }
    }

    public Object createGameHandle(Request req, Response res, AuthDAO aDao, GameDAO gDao) {

        String authToken = req.headers(ConfigConsts.LOGOUT_REQ_HEADER);   // Get authToken for verification

        // Verify the authToken for authorization
        if (!aDao.hasAuth(authToken)) { // If authToken isn't in database, return error status message
            res.status(401);
            return gs.toJson(new MessageData("Error: unauthorized"));
        }
        // Gather needed information from json request body
        GameRequestData name = gs.fromJson(req.body(), GameRequestData.class);
        try {
            int gameID = GameService.createGame(name.gameName(), gDao);
            res.status(200);
            return gs.toJson(new GameIDData(gameID));
        } catch (DataAccessException dataEx) {
            res.status(403);
            return gs.toJson(new MessageData("Error: No more available games"));
        } catch (BadRequestException badEx) {
            res.status(400);
            return gs.toJson(new MessageData("Error: bad request"));
        } catch (SQLException e) {
            res.status(500);
            return gs.toJson(new MessageData("Error: SQL malfunction."));
        }
    }

    public Object joinGameHandle(Request req, Response res, AuthDAO aDao, GameDAO gDao) {

        String authToken = req.headers(ConfigConsts.LOGOUT_REQ_HEADER);   // Get authToken for verification
        // Verify the authToken for authorization
        if (!aDao.hasAuth(authToken)) { // If authToken isn't in database, return error status message
            res.status(401);
            return gs.toJson(new MessageData("Error: unauthorized"));
        }
        // Gather the required information from json request body
        GameRequestData joinGameData = gs.fromJson(req.body(), GameRequestData.class);

        try {
            GameService.joinGame(joinGameData.gameID(), joinGameData.playerColor(), authToken, aDao, gDao);
            res.status(200);
            return "{}";    // No return body, give empty json
        } catch (BadRequestException badEx) {   // If gameID didn't exist or color was invalid
            res.status(400); // Set error code for bad request
            return gs.toJson(new MessageData("Error: bad request"));
        } catch (AlreadyTakenException alrEx) { // If requested team color is already claimed
            res.status(403); // Set error code for already-taken exception
            return gs.toJson(new MessageData("Error: already taken"));
        } catch (SQLException e) {
            res.status(500);
            return gs.toJson(new MessageData("Error: SQL problem in joinGame."));
        }
    }
}
