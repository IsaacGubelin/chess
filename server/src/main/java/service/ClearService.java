package service;

import dataAccess.*;
import server.DatabaseDAOCollection;

public class ClearService {

    // Clears all three databases: Auth, Games, and Users
    public static void clearService(GameDAO gDao, UserDAO uDao, AuthDAO aDao) {

        gDao.clearGamesDataBase();
        uDao.clearUserDatabase();
        aDao.clearAuthDatabase();
    }

}