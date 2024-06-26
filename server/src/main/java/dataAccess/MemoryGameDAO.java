package dataAccess;

import chess.ChessGame;
import config.ConfigConsts;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    private HashMap<Integer, GameData> gamesTable = new HashMap<>();

    // A game ID generator that automatically finds available ID numbers.
    private int generateID() {
        int id = ConfigConsts.GAME_ID_MIN;    // Starting ID value
        while (gamesTable.containsKey(id)) { // Increment if the id value is already taken
            id++;
            if (id > ConfigConsts.GAME_ID_MAX) // Will return -1 if out of available games
                return -1;
        }
        return id;
    }

    // Clear all GameData entries from database
    @Override
    public void clearGamesDataBase() {
        gamesTable.clear();
    }

    // Create and add a new chess game to the database
    @Override
    public int createGame(String gameName) {
        int id = generateID(); // Make new game ID

        GameData game = new GameData(id, null, null, gameName, new ChessGame());
        gamesTable.put(game.gameID(), game);
        return id;
    }

    // A getter for retrieving the games database
    @Override
    public ArrayList<GameData> getGamesList() {
        ArrayList<GameData> games = new ArrayList<>();
        for (int id : gamesTable.keySet()) { // Iterate through each key
            games.add(gamesTable.get(id)); // Fetch game from current key and add to list
        }
        return games;
    }

    // Use to update the white team with a new user
    @Override
    public void updateWhiteUsername(int gameID, String whiteUsername) {
        String blackUsername = gamesTable.get(gameID).blackUsername();  // Use for updated game record
        String gameName = gamesTable.get(gameID).gameName();            // Use for updated game record
        GameData game = new GameData(gameID, whiteUsername, blackUsername, gameName, gamesTable.get(gameID).game());
        gamesTable.put(gameID, game);   // Overwrite old game data with new data
    }

    // Use to update a new user for the black team
    @Override
    public void updateBlackUsername(int gameID, String blackUsername) {
        String whiteUsername = gamesTable.get(gameID).whiteUsername();  // Use for updated game record
        String gameName = gamesTable.get(gameID).gameName();            // Use for updated game record
        GameData game = new GameData(gameID, whiteUsername, blackUsername, gameName, gamesTable.get(gameID).game());
        gamesTable.put(gameID, game);   // Overwrite old data with new data
    }

    @Override
    public boolean hasGame(int gameID) {
        return gamesTable.containsKey(gameID);
    }
    @Override
    public boolean isEmpty() {
        return gamesTable.isEmpty();
    }

    @Override
    public boolean hasAvailableTeam(int gameID, String team) {
        if (team.equals(ConfigConsts.BLACK_TEAM_COL)) {   // If black is requested, check if black team username is null
            return gamesTable.get(gameID).blackUsername() == null;
        } else if (team.equals(ConfigConsts.WHITE_TEAM_COL)) {    // Check same condition for white team
            return gamesTable.get(gameID).whiteUsername() == null;
        }
        return false;   // Irregular team name input will return false by default
    }
}