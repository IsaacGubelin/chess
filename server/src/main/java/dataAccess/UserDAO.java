package dataAccess;

import exception.AlreadyTakenException;
import exception.DataAccessException;
import model.UserData;

public interface UserDAO {

    void clearUserDatabase();

    void createUser(UserData userData) throws AlreadyTakenException;

    UserData getUser(String username) throws DataAccessException;

}