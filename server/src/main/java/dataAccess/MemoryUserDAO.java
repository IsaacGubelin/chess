package dataAccess;

import model.UserData;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {


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

    /**
     * This constructor builds the user table in the SQL chess database.
     * @throws SQLDataException
     */
    public MemoryUserDAO() throws SQLDataException {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createUserTableStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // DATABASE OF USERS
    public HashMap<String, UserData> userTable = new HashMap<>();

    // FIXME: MAKE SQL
    // Evaluates if a given username exists
    public boolean hasThisUsername(String username) {
        return userTable.containsKey(username);
    }

    // FIXME: MAKE SQL
    // Clear the user database
    public void clearUserDatabase() {

        userTable.clear();
    }

    // FIXME: MAKE SQL
    public void createUser(UserData userData) throws AlreadyTakenException {
        if (hasThisUsername(userData.username())) {
            throw new AlreadyTakenException("Error: Cannot create new user. User already exists");
        }
        userTable.put(userData.username(), userData);
    }

    // FIXME: MAKE SQL
    // Get a user from a given username
    public UserData getUser(String username) {
        return userTable.get(username);
    }

}