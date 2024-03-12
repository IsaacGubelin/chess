package dataAccess;

import exception.AlreadyTakenException;
import exception.DataAccessException;
import model.UserData;

import java.sql.SQLException;

public interface UserDAO {

    void clearUserDatabase();

    void createUser(UserData userData) throws AlreadyTakenException, SQLException;

    UserData getUser(String username);

    boolean hasThisUsername(String username);

}