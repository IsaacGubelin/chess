import resException.ResponseException;
import ui.ClientUI;


import chess.*;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";    // Default URL
        if (args.length == 1) {                     // Use URL from argument if provided
            serverUrl = args[0];
        }

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);


        ClientUI clientUi = null;   // User Interface object
        try {
            clientUi = new ClientUI(serverUrl);
            clientUi.runInterface();
        } catch (ResponseException e) {
            System.out.println("Error: exception thrown during initialization. Server may not be online.");
        }

    }
}