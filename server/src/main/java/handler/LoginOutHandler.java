package handler;

import com.google.gson.Gson;
import config.ConfigConsts;
import dataAccess.*;
import exception.BadRequestException;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.AuthData;
import model.MessageData;
import model.UserData;

import service.LoginOutService;
import spark.Request;
import spark.Response;
public class LoginOutHandler {

    // LOGIN HANDLER
    public Object loginHandle(Request req, Response res, UserDAO uDao, AuthDAO aDao) {
        UserData user = new Gson().fromJson(req.body(), UserData.class);    // Convert Json to user data

        // This will have the login service verify that the user exists and the password is correct.
        try {
            AuthData auth = LoginOutService.login(user, uDao, aDao);
            res.status(200);    // Success code
            return new Gson().toJson(auth); // Make auth data response body
        } catch (UnauthorizedException unEx) {  // If password is incorrect or user doesn't exist
            res.status(401);            // Set status value to error code
            return new Gson().toJson(new MessageData("Error: unauthorized"));    // Return error message
        } catch (BadRequestException badEx) {   // If username or password data is null
            res.status(400);
            return new Gson().toJson(new MessageData("Error: bad request"));
        } catch (DataAccessException dEx) {
            res.status(500);
            return new Gson().toJson(new MessageData("Error: SQL Database issue"));
        }
    }

    // LOGOUT HANDLER
    public Object logoutHandle(Request req, Response res, AuthDAO aDao) {
        String authToken = req.headers(ConfigConsts.LOGOUT_REQ_HEADER);

        try {
            LoginOutService.logout(authToken, aDao);
            res.status(200); // Success
            return "{}";
        } catch (UnauthorizedException unEx) {
            res.status(401);
            return new Gson().toJson(new MessageData("Error: unauthorized"));
        } catch (DataAccessException e) {
            res.status(500);
            return new Gson().toJson(new MessageData("Error: SQL malfunction."));
        }

    }
}
