package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    // DATABASE OF USERS
    HashMap<String, UserData> userTable = new HashMap<>();

    // Evaluates if a given username exists
    public boolean hasThisUsername(String username) {
        return userTable.containsKey(username);
    }


    // Clear the user database
    public void clearUserDatabase() {
        userTable.clear();
    }

    //FIXME: Finish createUser function
    public void createUser(UserData userData) throws DataAccessException {

    }

    // Get a user from a given username
    public UserData getUser(String username) throws DataAccessException {
        // Check that user exists
        if (!hasThisUsername(username)) {
            throw new DataAccessException("Error: username not in database.");
        }
        return userTable.get(username);
    }

}