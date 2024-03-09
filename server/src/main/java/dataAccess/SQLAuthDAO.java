package dataAccess;

import com.google.gson.Gson;
import exception.DataAccessException;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{

    private final String[] createAuthTableStatements = { // FIXME: Change statements
            """
            CREATE TABLE IF NOT EXISTS  auths
            (
              `authToken` varchar(256),
              `username` varchar(256),
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

    // Generate a unique authToken
    private String generateToken() {
        String token = UUID.randomUUID().toString();    // Make an authToken
        return token;
    }

    @Override
    public void clearAuthDatabase() {
        String clearStmt = "TRUNCATE TABLE auths";
        try {
            ExecuteSQL.executeSqlLine(clearStmt);
        } catch (Exception e) {
            System.out.println("Error: could not clear auth table from chess database.");
        }
    }

    @Override
    public String createAuth(String username) {
        String token = generateToken();
        String createStmt = "INSERT INTO auths (authToken, username) VALUES ("
                            + token + ", " + username + ")";
        try {
            ExecuteSQL.executeSqlLine(createStmt);
        } catch (Exception e) {
            System.out.println("Error: could not create auth.");
        }
        return token;
    }

    @Override
    public AuthData getAuth(String authToken) throws exception.DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, json FROM auths WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: could not retrieve auth.");
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        String deleteStmt = "DELETE FROM auths WHERE authToken=" + authToken;
        try {
            ExecuteSQL.executeSqlLine(deleteStmt);
        } catch (Exception e) {
            System.out.println("Error: could not delete authToken.");
        }
    }


    // Read output from SQL query for auth data
    private AuthData readAuth(ResultSet rs) throws SQLException {
        String name = rs.getString("username");
        String token = rs.getString("authToken");
        return new AuthData(token, name);
    }

}
