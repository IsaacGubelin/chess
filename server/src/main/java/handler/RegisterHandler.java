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

        UserData user = new Gson().fromJson(req.body(), UserData.class);    // Convert Json to user data

        try {
            String authToken = RegisterService.register(user, uDAO, aDAO);      // Get authToken
            res.status(200);                                           // Success code
            return new Gson().toJson(new AuthData(authToken, user.username())); // Make auth data response body
        } catch (BadRequestException badEx) {     // Bad requests are requests that are missing data
            res.status(400);            // Set status value to error code
            return new Gson().toJson(new MessageData("Error: bad request"));    // Return error message
        } catch (DataAccessException dataEx) {    // This exception happens when a username is already used
            res.status(403);            // Set status to error code
            return new Gson().toJson(new MessageData("Error: already taken")); // Return error message
        }
    }
}
