package dataAccess;

import exception.DataAccessException;
import model.AuthData;

public interface AuthDAO {



    void clearAuthDatabase();
    String createAuth(String username) throws exception.DataAccessException;

    AuthData getAuth(String authToken) throws exception.DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;
}