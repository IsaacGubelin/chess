package handler;

import dataAccess.*;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    // The handler method for deleting all entries from all databases
    public Object clearDatabases(Request req, Response res, GameDAO gDao, UserDAO uDao, AuthDAO aDao) {

        ClearService.clearService(gDao, uDao,aDao); // Call the clear service
        res.status(200); // Success
        return "{}";
    }

}
