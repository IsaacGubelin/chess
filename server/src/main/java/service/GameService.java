package service;

import config.Config;
import dataAccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.DataAccessException;
import model.GameData;
import server.DatabaseDAOCollection;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class GameService {


    // Makes a list of all games in the games database and returns a collection
    public static Collection<GameData> getGames(DatabaseDAOCollection daos) {
        HashSet<GameData> games = new HashSet<>(); // Fill this container with all games
        for (int id : daos.memGameDAO.getGameDatabase().keySet()) { // Iterate through each key
            games.add(daos.memGameDAO.getGameDatabase().get(id)); // Fetch game from current key and add to list
        }
        return games;   // Return the collection of games
    }

    public static int createGame(String gameName, GameDAO gDao)
            throws DataAccessException, BadRequestException, SQLException {

        if (gameName == null) {     // Make sure gameName parameter has data
            throw new BadRequestException("Error: bad request");
        }
        int gameID = gDao.createGame(gameName); // This function returns the game ID when it makes a new game
        if (gameID == -1) {
            throw new DataAccessException("Error: Games are full");
        }
        return gameID;  // If game was created successfully, return the new game ID
    }

    // joinGame service. This needs the authDAO for retrieving the username and the gameDAO for adding user to game.
    public static void joinGame(int gameID, String color, String authToken, AuthDAO aDao, GameDAO gDao)
            throws AlreadyTakenException, BadRequestException, SQLException {

        if (!gDao.hasGame(gameID)) {  // If the requested game doesn't exist
            throw new BadRequestException("Error: specified game ID does not exist.");
        }

        // SPECTATE: If color is null, user wants to join game as spectator
        if (color == null) {
            // Add logic here for spectating
        }
        try {
            String username = aDao.getAuth(authToken).username();   // Retrieve username listed under authToken


            // Join user to black team of specified game
            if (color.equals(Config.BLACK_TEAM_REQ)) {
                // Check if the requested game has an open slot for the black team
                if (!gDao.hasAvailableTeam(gameID, Config.BLACK_TEAM_COL)) // If black team isn't available
                    throw new AlreadyTakenException("Black team already taken.");
                gDao.updateBlackUsername(gameID, username); // If good to do so, update black team name
            }
            // Join user to white team
            else if (color.equals(Config.WHITE_TEAM_REQ)) {
                // Check if requested game has an open slot for the white team
                if (!gDao.hasAvailableTeam(gameID, Config.WHITE_TEAM_COL)) // If white team isn't available
                    throw new AlreadyTakenException("White team already taken.");
                gDao.updateWhiteUsername(gameID, username); // Update white team name
            } else  // If none of previous conditions were met, request is bad.
                throw new BadRequestException("Given color does not match options.");
        } catch (DataAccessException | SQLException e) {
            throw new SQLException("SQL Error.");
        }
    }


}