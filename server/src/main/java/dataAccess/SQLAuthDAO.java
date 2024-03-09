package dataAccess;

import exception.DataAccessException;
import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{

    private final String[] createAuthTableStatements = { // FIXME: Change statements
            """
            CREATE TABLE IF NOT EXISTS  auths
            (
              `authToken` varchar(256),
              `username` varchar(256),
              `gameName` varchar(256),
              PRIMARY KEY (`authToken`)
            );
            """
    };

    // Constructor for AuthDAO
    public SQLAuthDAO() {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createAuthTableStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (exception.DataAccessException e) {
            System.out.println("Error: could not create auths database.");
        }
    }

    @Override
    public void clearAuthDatabase() {

    }

    @Override
    public String createAuth(String username) throws exception.DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws exception.DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
