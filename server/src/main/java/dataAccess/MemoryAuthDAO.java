package dataAccess;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private HashMap<String, String> authDataTable = new HashMap<>();
    // FIXME: Check how to properly initialize this data structure

    // Generate an authToken
    private String generateToken() {
        return UUID.randomUUID().toString();
    }


    /**
     * Clears the authToken data structure
     */
    public void clearAuthDatabase() {
         authDataTable.clear();
    }

    // Add these methods:


    // createAuth
    // getAuth
    // deleteAuth

    public void createAuth() {

    }

    // Use DataAccessException if user tries to plug in something that already exists
    // Or removes something that doesn't exist

}