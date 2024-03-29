import chess.*;

import ui.UI;

/**
 * LAST LEFT OFF:
 * Adding port option for facade constructor
 * Making Junit tests
 * Weird error with joining/observing last game in list
 */


public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";    // Default URL
        if (args.length == 1) {                     // Use URL from argument if provided
            serverUrl = args[0];
        }

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        UI ui = new UI(serverUrl);   // User Interface object
        ui.runInterface();

    }
}