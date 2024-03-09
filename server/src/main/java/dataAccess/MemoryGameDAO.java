package dataAccess;

import chess.ChessGame;
import config.Config;
import model.GameData;

import java.sql.SQLException;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    //TODO: Remove when SQL implementation is complete
    private HashMap<Integer, GameData> gamesTable = new HashMap<>();

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

    // Clear all GameData entries from database
    public void clearGamesDataBase() {
        gamesTable.clear();
    }

    // Create and add a new chess game to the database
    public int createGame(String gameName) {
        int id = generateID(); // Make new game ID

        GameData game = new GameData(id, null, null, gameName, new ChessGame());
        gamesTable.put(game.gameID(), game);
        return id;
    }

    // A getter for retrieving the games database
    public HashMap<Integer, GameData> getGameDatabase() {
        return gamesTable;
    }

    // Use to update the white team with a new user
    public void updateWhiteUsername(int gameID, String whiteUsername) {
        String blackUsername = gamesTable.get(gameID).blackUsername();  // Use for updated game record
        String gameName = gamesTable.get(gameID).gameName();            // Use for updated game record
        GameData game = new GameData(gameID, whiteUsername, blackUsername, gameName, gamesTable.get(gameID).game());
        gamesTable.put(gameID, game);   // Overwrite old game data with new data
    }

    // Use to update a new user for the black team
    public void updateBlackUsername(int gameID, String blackUsername) {
        String whiteUsername = gamesTable.get(gameID).whiteUsername();  // Use for updated game record
        String gameName = gamesTable.get(gameID).gameName();            // Use for updated game record
        GameData game = new GameData(gameID, whiteUsername, blackUsername, gameName, gamesTable.get(gameID).game());
        gamesTable.put(gameID, game);   // Overwrite old data with new data
    }

}