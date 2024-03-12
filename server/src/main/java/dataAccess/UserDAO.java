package dataAccess;

import exception.AlreadyTakenException;
import exception.DataAccessException;
import model.UserData;

import java.sql.SQLException;

public interface UserDAO {

    void clearUserDatabase();

    void createUser(UserData userData) throws SQLException;

    UserData getUser(String username);

    void deleteUser(String username) throws DataAccessException;

    boolean hasThisUsername(String username);

}