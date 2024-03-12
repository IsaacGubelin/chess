package service;

import dataAccess.*;
import exception.BadRequestException;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.AuthData;
import server.DatabaseDAOCollection;

import java.sql.SQLException;

public class LoginOutService {
    public static AuthData login(model.UserData user, UserDAO uDao, AuthDAO aDao)
            throws UnauthorizedException, BadRequestException, DataAccessException {

        // Check for bad request
        if (user.username() == null || user.password() == null) {
            throw new BadRequestException("Error: Missing data field(s) in login.");
        }

        // Check that authToken exists
        if (!uDao.hasThisUsername(user.username())) {
            throw new UnauthorizedException("Error: User not registered in database.");
        }
        // Now check if password matches
        else if (!uDao.getUser(user.username()).password().equals(user.password())) {
            throw new UnauthorizedException("Incorrect password.");
        }
        // User exists and password matches. Create new auth data and claim the returned token.
        try {
            String authToken = aDao.createAuth(user.username());
            return new AuthData(authToken, user.username());    // Return the auth data to the handler
        } catch (DataAccessException | SQLException ex) {
            throw new DataAccessException("SQL auth DAO error.");
        }
    }
    public static void logout(String authToken, AuthDAO aDao) throws UnauthorizedException, DataAccessException {
        if (!aDao.hasAuth(authToken)) {
            throw new UnauthorizedException("Error: Cannot find given authToken in database.");
        }
        aDao.deleteAuth(authToken);
    }


}
