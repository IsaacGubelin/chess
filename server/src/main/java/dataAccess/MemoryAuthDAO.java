package dataAccess;

import java.util.HashSet;
public class MemoryAuthDAO implements AuthDAO {

    private Hashset<AuthData> authTokenTable;



    // TODO:

    // Create
    // Read
    // Update
    // Delete


    /**
     * Clears the authToken data structure
     */
    public void clearAuthTable() {
        authTokenTable.clear();
    }

    // Add these methods:

    // createUser
    // getUser
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