package dataAccess;

import exception.DataAccessException;
import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {



    void clearAuthDatabase();
    String createAuth(String username) throws exception.DataAccessException, SQLException;

    AuthData getAuth(String authToken) throws exception.DataAccessException;

    boolean hasAuth(String authToken);

    void deleteAuth(String authToken) throws DataAccessException;

    boolean isEmpty() throws DataAccessException;
}