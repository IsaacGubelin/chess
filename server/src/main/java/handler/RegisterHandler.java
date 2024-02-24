package handler;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.UserData;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    //TODO:
    // Convert gson/json into a user record
    // Send to the RegisterService class
    // Get the authToken from the return
    // Make a response body?

    // The handler for registering a new user.
//    public Object registerHandle(Request req, Response res, MemoryUserDAO uDAO, MemoryAuthDAO aDAO) {
//
//        // FIXME: Convert json and load into this thing
//        UserData user;
//
//        try {
//            RegisterService.register(user, uDAO, aDAO);
//        } catch (DataAccessException ex) {
//            // TODO: This is where failure codes are set to res
//        }
//
//        //FIXME: Return body
//
//    }
}
