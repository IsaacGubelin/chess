package dataAccess;

import chess.ChessGame;
import config.Config;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    // A game ID generator that automatically finds available ID numbers.
    private int generateID() {
        int id = Config.GAME_ID_MIN;    // Starting ID value
        while (gamesTable.containsKey(id)) { // Increment if the id value is already taken
            id++;
            if (id > Config.GAME_ID_MAX) // Will return -1 if out of available games
                return -1;
        }
        return id;
    }

    // Games database
    private HashMap<Integer, GameData> gamesTable = new HashMap<>();


    // Clear all GameData entries from database
    public void clearGamesDataBase() {
        gamesTable.clear();
    }

    // Create and add a new chess game to the database
    public int createGame(String gameName) {
        // Make new game ID
        int id = generateID();

        // FIXME: Change GameData to record
        // TODO: Construct a GameData object and insert into map with ID as key.

//        gamesTable.put(game.gameID(), game);
        return id;
    }

    // Returns the chess game object associated with the given ID
    public ChessGame getGame(String id) throws DataAccessException {
        // Handle case where game ID is not in database
        if (!gamesTable.containsKey(id)) {
            throw new DataAccessException("Error: game ID does not exist.");
        }
        return gamesTable.get(id).game();
    }


}