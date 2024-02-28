package dataAccess;

import model.AuthData;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    public HashMap<String, AuthData> authDataTable = new HashMap<>();

    // Generate a unique authToken
    private String generateToken() {
        String token = UUID.randomUUID().toString();    // Make an authToken
        while (authDataTable.containsKey(token))
            token = UUID.randomUUID().toString();       // If the token already exists, make a new one
        return token;
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

    // Check if the database contains an authToken
    public boolean hasAuth(String authToken) {
        return authDataTable.containsKey(authToken);
    }

    // Retrieve auth data from a given authToken
    public AuthData getAuth(String authToken) {
        return authDataTable.get(authToken);
    }


    // Remove an authData object from the database, given the corresponding token
    public void deleteAuth(String authToken) {
        authDataTable.remove(authToken);
    }

}