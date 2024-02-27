package dataAccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO {

    void clearUserDatabase();

    void createUser(UserData userData) throws AlreadyTakenException;

    UserData getUser(String username) throws DataAccessException;

}