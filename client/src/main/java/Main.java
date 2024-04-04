import chess.*;

import ui.ChessBoardPrint;
import ui.EscapeSequences;
import ui.UI;

import java.util.Collection;
import java.util.HashSet;

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

//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);

//        ChessGame game = new ChessGame();
//        try {
//            game.makeMove(new ChessMove(new ChessPosition(2, 5), new ChessPosition(3, 5)));
//            game.makeMove(new ChessMove(new ChessPosition(7, 1), new ChessPosition(6, 1)));
//            game.makeMove(new ChessMove(new ChessPosition(1, 4), new ChessPosition(2, 5)));
//            game.makeMove(new ChessMove(new ChessPosition(7, 4), new ChessPosition(6, 4)));
//            game.makeMove(new ChessMove(new ChessPosition(2, 5), new ChessPosition(6, 1)));
//            game.makeMove(new ChessMove(new ChessPosition(2, 1), new ChessPosition(3, 1)));
//        } catch (InvalidMoveException e) {
//            System.out.println("Whoops");
//        }
//        Collection<ChessMove> moves = game.validMoves(new ChessPosition(6, 1));
//        ChessBoardPrint.printChessBoard(game.getBoard(), true);

        UI ui = new UI(serverUrl);   // User Interface object
        ui.runInterface();

    }
}