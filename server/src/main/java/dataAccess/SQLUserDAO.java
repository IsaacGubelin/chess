package dataAccess;

import exception.AlreadyTakenException;
import exception.DataAccessException;
import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{

    /**
     * SQL statements for generating new table called "users"
     */
    private final String[] createUserTableStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users
            (
              `username` varchar(256),
              `password` varchar(256),
              `email` varchar(256),
              PRIMARY KEY (`username`)
            );
            """
    };

    public SQLUserDAO() {

        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createUserTableStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | exception.DataAccessException e) {
            System.out.println("Error: could not create users database.");
        }
    }
    @Override
    public void clearUserDatabase() {

    }

    @Override
    public void createUser(UserData userData) throws AlreadyTakenException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
}
