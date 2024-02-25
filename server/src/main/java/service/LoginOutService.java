package service;

import dataAccess.*;
import model.AuthData;

import javax.xml.crypto.Data;

public class LoginOutService {

    //TODO:
    // Handler gets the json. Converts into object. Sends to this service.

    public static AuthData login(model.UserData user, MemoryUserDAO uDAO, MemoryAuthDAO aDAO)
            throws UnauthorizedException, BadRequestException {

        // Check for bad request
        if (user.username() == null || user.password() == null) {
            throw new BadRequestException("Error: Missing data field(s) in login.");
        }

        // Check that authToken exists
        if (!uDAO.hasThisUsername(user.username())) {
            throw new UnauthorizedException("Error: User not registered in database.");
        }
        // Now check if password matches
        else if (!uDAO.getUser(user.username()).password().equals(user.password())) {
            throw new UnauthorizedException("Incorrect password.");
        }
        // User exists and password matches. Create new auth data and claim the returned token.
        String authToken = aDAO.createAuth(user.username());
        return new AuthData(authToken, user.username());    // Return the auth data to the handler
    }
    public static void logout(String authToken, MemoryAuthDAO aDAO) throws UnauthorizedException {
        if (!aDAO.hasAuth(authToken)) {
            throw new UnauthorizedException("Error: Cannot find given authToken in database.");
        }
        aDAO.deleteAuth(authToken);
    }


}
