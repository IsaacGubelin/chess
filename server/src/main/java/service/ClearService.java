package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

public class ClearService {

    // Clears all three databases: Auth, Games, and Users
    public static void clearService(MemoryUserDAO uDAO, MemoryGameDAO gDAO, MemoryAuthDAO aDAO) {
        uDAO.clearUserDatabase();
        gDAO.clearGamesDataBase();
        aDAO.clearAuthDatabase();
    }
}