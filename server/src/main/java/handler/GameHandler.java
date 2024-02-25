package handler;

import com.google.gson.Gson;
import config.Config;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.MessageData;
import spark.Request;
import spark.Response;

public class GameHandler {

    public Object listGamesHandle(Request req, Response res, MemoryAuthDAO aDAO, MemoryGameDAO gDAO) {

        String authToken = req.headers(Config.LOGOUT_REQ_HEADER);   // Get authToken for verification

        // Verify the authToken for authorization
        if (!aDAO.hasAuth(authToken)) { // If authToken isn't in database, return error status message
            res.status(401);
            return new Gson().toJson(new MessageData("Error: unauthorized"));
        }

        //FIXME:
        return null;
    }

    public Object createGameHandle(Request req, Response res, MemoryAuthDAO aDAO, MemoryGameDAO gDAO) {

        String authToken = req.headers(Config.LOGOUT_REQ_HEADER);   // Get authToken for verification

        // Verify the authToken for authorization
        if (!aDAO.hasAuth(authToken)) { // If authToken isn't in database, return error status message
            res.status(401);
            return new Gson().toJson(new MessageData("Error: unauthorized"));
        }
        // FIXME:

        return null;
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
