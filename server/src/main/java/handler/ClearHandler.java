package handler;

import dataAccess.*;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    // The handler method for deleting all entries from all databases
    public Object clearDatabases(Request req, Response res, MemoryUserDAO uDAO, MemoryGameDAO gDAO, MemoryAuthDAO aDAO){

        ClearService.clearService(uDAO, gDAO, aDAO); // Call the clear service
        res.status(200); // Success
        return "{}";
    }

}
