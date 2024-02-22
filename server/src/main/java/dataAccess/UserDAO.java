package dataAccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO {

    void clearUserDatabase();

    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

}