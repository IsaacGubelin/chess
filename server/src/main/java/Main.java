import chess.*;
import exception.DataAccessException;
import dataAccess.DatabaseManager;
import server.Server;


// FIXME: Change DAO types in server and remove multi-DAO object class entirely

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        Server server = new Server();   // Generic server object

        try {                           // Build database and tables if not made already
            server.run(8080);
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            System.out.println("Unable to create database.");
        }

    }
}