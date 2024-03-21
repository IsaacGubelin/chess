import chess.*;
import ui.ChessBoardPrint;
import ui.UI;

/**
 * LAST LEFT OFF:
 * Print boards working!!!
 */


public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        UI ui = new UI();   // User Interface object

        var game = new ChessGame();
        try {
            game.makeMove(new ChessMove(new ChessPosition(2,1), new ChessPosition(3, 1)));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        ChessBoardPrint.printChessWhitePerspective(game.getBoard());

    }
}