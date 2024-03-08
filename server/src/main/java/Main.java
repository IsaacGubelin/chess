import chess.*;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import server.Server;

import java.sql.SQLDataException;


public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        Server server = new Server();

        // Build database and tables if not made already
        try {
            server.initializeDAOs();
            DatabaseManager.createDatabase();
        } catch (SQLDataException e) {
            System.out.println("Unable to initialize DAOs.");
        } catch (DataAccessException e) {
            System.out.println("Unable to create database.");
        }

        // Run the server
        server.run(8080);
    }

}