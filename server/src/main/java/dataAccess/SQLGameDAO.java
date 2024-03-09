package dataAccess;

import exception.DataAccessException;

import java.sql.SQLException;

public class SQLGameDAO implements GameDAO{

    // For creating game table in chess database
    private final String[] createGameTableStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games
            (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256),
              `game` varchar(8192),
              PRIMARY KEY (`gameID`)
            );
            """
    };

    public SQLGameDAO() {

        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createGameTableStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("SQL Error: could not make games database.");
        }
    }
    @Override
    public void clearGamesDataBase() {

    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }
}
