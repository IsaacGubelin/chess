package dataAccess;

import exception.AlreadyTakenException;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    // DATABASE OF USERS
    public HashMap<String, UserData> userTable = new HashMap<>();

    // Evaluates if a given username exists
    @Override
    public boolean hasThisUsername(String username) {
        return userTable.containsKey(username);
    }

    // Clear the user database
    @Override
    public void clearUserDatabase() {
        userTable.clear();
    }

    @Override
    public void createUser(UserData userData) {

        userTable.put(userData.username(), userData);
    }

    // Get a user from a given username
    @Override
    public UserData getUser(String username) {
        return userTable.get(username);
    }


    // Remove a user
    @Override
    public void deleteUser(String username) {
        userTable.remove(username);
    }
}