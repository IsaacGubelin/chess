package service;


import dataAccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;

public class RegisterService {
    public static String register(model.UserData user, MemoryUserDAO uDAO, MemoryAuthDAO aDAO)
            throws AlreadyTakenException, BadRequestException {

        // Check for bad request (if any field is null)
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestException("Bad Request: missing info field in user data.");
        }


        // Check that username is valid
        if (uDAO.hasThisUsername(user.username())) {
            throw new AlreadyTakenException("Error: Username already taken.");
        }
        // Create and add user to database
        uDAO.createUser(user);

        // Add new auth to auth database and get a new authToken
        String authToken = aDAO.createAuth(user.username());
        return authToken;
    }

}