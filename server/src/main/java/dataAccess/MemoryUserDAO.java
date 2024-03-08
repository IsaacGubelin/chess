package dataAccess;

import model.UserData;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    // DATABASE OF USERS
    public HashMap<String, UserData> userTable = new HashMap<>();

    /**
     * This constructor builds the user table in the SQL chess database.
     * @throws SQLDataException
     */




    // FIXME: MAKE SQL
    // Evaluates if a given username exists
    public boolean hasThisUsername(String username) {
        return userTable.containsKey(username);
    }

    // FIXME: MAKE SQL
    // Clear the user database
    public void clearUserDatabase() {

        String statement = "TRUNCATE users"; // For erasing users table in SQL


        userTable.clear();
    }

    // FIXME: MAKE SQL
    public void createUser(UserData userData) throws AlreadyTakenException {
        if (hasThisUsername(userData.username())) {
            throw new AlreadyTakenException("Error: Cannot create new user. User already exists");
        }
        userTable.put(userData.username(), userData);
    }

    // FIXME: MAKE SQL
    // Get a user from a given username
    public UserData getUser(String username) {
        return userTable.get(username);
    }

}