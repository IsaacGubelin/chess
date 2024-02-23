package dataAccess;

import model.AuthData;

public interface AuthDAO {

    void clearAuthDatabase();
    void createAuth() throws DataAccessException;

//    AuthData getAuth() throws DataAccessException;

//    void deleteAuth() throws DataAccessException;
}