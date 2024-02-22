package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    // Games database
    private HashMap<String, GameData> gamesTable = new HashMap<>();


    // Clear all GameData entries from database
    public void clearGamesDataBase() {
        gamesTable.clear();
    }

    // TODO: createGame

    // Returns the chess game object associated with the given ID
    public ChessGame getGame(String id) throws DataAccessException {
        // Handle case where game ID is not in database
        if (!gamesTable.containsKey(id)) {
            throw new DataAccessException("Error: game ID does not exist.");
        }
        return gamesTable.get(id).game();
    }


}