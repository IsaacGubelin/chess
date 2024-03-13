package dataAccess;

import java.sql.SQLException;

import exception.AlreadyTakenException;
import exception.DataAccessException;

public interface GameDAO {

    void clearGamesDataBase();
    int createGame(String gameName) throws SQLException;

    void updateWhiteUsername(int gameID, String whiteUsername) throws SQLException, AlreadyTakenException;

    void updateBlackUsername(int gameID, String whiteUsername) throws SQLException, AlreadyTakenException;

    boolean hasGame(int gameID);

    boolean hasAvailableTeam(int gameID, String team) throws DataAccessException;

    boolean isEmpty() throws DataAccessException;
}