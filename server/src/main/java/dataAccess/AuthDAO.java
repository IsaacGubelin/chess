package dataAccess;

import model.AuthData;

public interface AuthDAO {

    void clearAuthDatabase();
    String createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;
}