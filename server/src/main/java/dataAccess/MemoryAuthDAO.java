package dataAccess;

import java.util.HashMap;
public class MemoryAuthDAO implements AuthDAO {

    private HashMap<String, String> authDataTable = new HashMap<>();
    // FIXME: Check how to properly initialize this data structure



    // TODO:

    // Create
    // Read
    // Update
    // Delete


    /**
     * Clears the authToken data structure
     */
    public void clearAuthTable() {
         authDataTable.clear();
    }

    // Add these methods:

    // createGame
    // getGame
    // listGames
    // updateGame
    // createAuth
    // getAuth
    // deleteAuth

    // Use DataAccessException if user tries to plug in something that already exists
    // Or removes something that doesn't exist

}