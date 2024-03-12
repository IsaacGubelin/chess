package server;

import dataAccess.*;

public class DatabaseDAOCollection {

    // Collection of regular memory DAOs. These erase when the server stops.
    public MemoryUserDAO memUserDAO = new MemoryUserDAO();
    public MemoryGameDAO memGameDAO = new MemoryGameDAO();
    public MemoryAuthDAO memAuthDAO = new MemoryAuthDAO();

    // Collection of SQL DAOs. These retain data even when server is offline.
    public SQLUserDAO sqlUserDAO = new SQLUserDAO();
    public SQLGameDAO sqlGameDAO = new SQLGameDAO();
    public SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
}
