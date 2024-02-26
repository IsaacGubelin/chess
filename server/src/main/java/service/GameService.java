package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryGameDAO;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.HashSet;

public class GameService {


    // Makes a list of all games in the games database and returns a collection
    public static Collection<ChessGame> getGames(MemoryGameDAO gDAO) {
        HashSet<ChessGame> games = new HashSet<>(); // Fill this container with all games
        for (int id : gDAO.getGameDatabase().keySet()) { // Iterate through each key
            games.add(gDAO.getGameDatabase().get(id).game()); // Fetch game from current key and add to list
        }
        return games;   // Return the collection of games
    }

    public static int createGame(String gameName, MemoryGameDAO gDAO) throws DataAccessException {
        int gameID = gDAO.createGame(gameName); // This function returns the game ID when it makes a new game
        if (gameID == -1) {
            throw new DataAccessException("Error: Games are full");
        }
        return gameID;  // If game was created successfully, return the new game ID
    }

    // TODO: joinGame service method


}