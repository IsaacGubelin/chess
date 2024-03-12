package service;


import dataAccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.DataAccessException;
import org.eclipse.jetty.server.Authentication;
import server.DatabaseDAOCollection;

import java.sql.SQLException;

public class RegisterService {
    public static String register(model.UserData user, UserDAO uDao, AuthDAO aDao)
            throws AlreadyTakenException, BadRequestException, SQLException {

        // Check for bad request (if any field is null)
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestException("Bad Request: missing info field in user data.");
        }


        // Check that username is valid
        if (uDao.hasThisUsername(user.username())) {
            throw new AlreadyTakenException("Error: Username already taken.");
        }
        // Create and add user to database
        try {
            uDao.createUser(user);
            // Add new auth to auth database and get a new authToken
            String authToken = aDao.createAuth(user.username());
            return authToken;

        } catch (SQLException | DataAccessException ex) {
            throw new BadRequestException("Could not create user in SQL.");
        }


    }

}