package server;

import dataAccess.*;

public class DatabaseDAOCollection {

    // Collection of regular memory DAOs. These erase when the server stops.
    private MemoryUserDAO memUserDAO = new MemoryUserDAO();
    private MemoryGameDAO memGameDAO = new MemoryGameDAO();
    private MemoryAuthDAO memAuthDAO = new MemoryAuthDAO();

    // Collection of SQL DAOs. These retain data even when server is offline.
    private SQLUserDAO sqlUserDAO = new SQLUserDAO();
    private SQLGameDAO sqlGameDAO = new SQLGameDAO();
    private SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
}
