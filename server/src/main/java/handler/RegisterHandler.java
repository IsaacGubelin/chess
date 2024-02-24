package handler;

import com.google.gson.Gson;
import dataAccess.BadRequestException;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.MessageData;
import model.UserData;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    // The handler for registering a new user.
    public Object registerHandle(Request req, Response res, MemoryUserDAO uDAO, MemoryAuthDAO aDAO) {

        UserData user = new Gson().fromJson(req.body(), UserData.class);
        String registerResBody = "";

        try {
            // Get authToken
            String authToken = RegisterService.register(user, uDAO, aDAO);
            res.status(200);                                                      // Success code
            return new Gson().toJson(new AuthData(authToken, user.username()));  // Make response body
        } catch (BadRequestException badEx) {
            res.status(400);
            return new Gson().toJson(new MessageData("Error: bad request"));
        } catch (DataAccessException dataEx) {
            res.status(403);
            return new Gson().toJson(new MessageData("Error: already taken"));
        }

        return registerResBody;
    }
}
