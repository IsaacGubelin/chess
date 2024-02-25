package handler;

import com.google.gson.Gson;
import config.Config;
import dataAccess.*;
import model.AuthData;
import model.MessageData;
import model.UserData;
import service.LoginOutService;
import service.RegisterService;
import spark.Request;
import spark.Response;
public class LoginOutHandler {

    // LOGIN HANDLER
    public Object loginHandle(Request req, Response res, MemoryUserDAO uDAO, MemoryAuthDAO aDAO) {
        UserData user = new Gson().fromJson(req.body(), UserData.class);    // Convert Json to user data

        // This will have the login service verify that the user exists and the password is correct.
        try {
            AuthData auth = LoginOutService.login(user, uDAO, aDAO);
            res.status(200);    // Success code
            return new Gson().toJson(auth); // Make auth data response body
        } catch (UnauthorizedException unEx) {  // If password is incorrect or user doesn't exist
            res.status(401);            // Set status value to error code
            return new Gson().toJson(new MessageData("Error: unauthorized"));    // Return error message
        } catch (BadRequestException badEx) {   // If username or password data is null
            res.status(400);
            return new Gson().toJson(new MessageData("Error: bad request"));
        }
    }

    // LOGOUT HANDLER
    public Object logoutHandle(Request req, Response res, MemoryAuthDAO aDAO) {
        String authToken = req.headers(Config.LOGOUT_REQ_HEADER);

        // Verify the authToken for authorization
        if (!aDAO.hasAuth(authToken)) { // If authToken isn't in database, return error status message
            res.status(401);
            return new Gson().toJson(new MessageData("Error: unauthorized"));
        }

        try {
            LoginOutService.logout(authToken, aDAO);
            res.status(200); // Success
            return "{}";
        } catch (UnauthorizedException unEx) {
            res.status(401);
            return new Gson().toJson(new MessageData("Error: unauthorized"));
        }

    }


}
