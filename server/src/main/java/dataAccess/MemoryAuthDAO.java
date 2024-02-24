package dataAccess;

import model.AuthData;


import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private HashMap<String, AuthData> authDataTable = new HashMap<>();

    // Generate an authToken
    private String generateToken() {
        return UUID.randomUUID().toString();
    }


    // Clear the auth database
    public void clearAuthDatabase() {
         authDataTable.clear();
    }

    // Add a new authToken and auth data into database
    public String createAuth(String username) {
        String token = generateToken();                             // Make a new authToken
        authDataTable.put(token, new AuthData(token, username));    // Add new authData into database
        return token;                                               // Return new authToken
    }

    // Retrieve auth data from a given authToken
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (!authDataTable.containsKey(authToken)) {
            throw new DataAccessException("Error: Cannot retrieve auth data, token doesn't exist.");
        }
        return authDataTable.get(authToken);
    }

    // Remove an authData object from the database, given the corresponding token
    public void deleteAuth(String authToken) throws DataAccessException {
        if (!authDataTable.containsKey(authToken)) {
            throw new DataAccessException("Error: auth data does not exist. Cannot delete auth.");
        }
        authDataTable.remove(authToken);
    }

}