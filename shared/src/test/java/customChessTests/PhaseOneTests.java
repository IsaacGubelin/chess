package customChessTests;

import chess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static passoffTests.TestFactory.*;

/**
 * PHASE ONE TESTS CREATED BY ISAAC GUBELIN ON 02-02-2024
 * <p>
 * File: PhaseOneTests.java
 * <p>
 * This class tests for the KingCheckEvaluator
 */


// TODO: FOCUS ON CHESSGAME
    // TODO: FIX BUGS WITH REMAINING SEVEN TESTS


public class PhaseOneTests {

    @Test
    @DisplayName("Check potential risk")
    public void myTestFunc() {

    /*  |r|n|b|q| |b|n|r|
        |p|p|p|p|p|p|p|p|
        | | | | | | | |R|
        | | | |k| | | |R|
        | | | | | | | | |
        | | | |R| | | | |
        |P|P|P|P|P|P|P|P|
        |R|N|B|Q|K|B|N|R|   */

        ChessPosition position = getNewPosition(5, 4);
        var game = getNewGame();
        System.out.println(game.getBoard().getPiece(8, 5).toString());
        game.getBoard().removePiece(new ChessPosition(8, 5));

        game.getBoard().addPiece(position, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        game.getBoard().addPiece(5, 8, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        game.getBoard().addPiece(6, 8, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
//        game.getBoard().addPiece(4, 8, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        game.getBoard().addPiece(3, 4, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

//        game.getBoard().addPiece(4, 4, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

        game.getBoard().printBoard();

        HashSet<ChessMove> moves = (HashSet<ChessMove>) game.validMoves(position);

        System.out.println("Valid moves: " + moves.size());
        for (ChessMove move : moves) {
            System.out.println(move.toString());
        }



    }

    @Test
    @DisplayName("Check potential risk")
    public void testCasesFunc() {

    /*  |r|n|b|q| |b|n|r|
        |p|p|p|p|p|p|p|p|
        | | | | | | | |R|
        | | | | | | | |R|
        | | ||kt| | | | |
        | | | |R| | | | |
        |P|P|P|P|P|P|P|P|
        |R|N|B|Q|K|B|N|R|   */


        var game = getNewGame();

        game.getBoard().removePiece(new ChessPosition(8, 5));

        game.getBoard().addPiece(4, 3, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        game.getBoard().addPiece(5, 8, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        game.getBoard().addPiece(6, 8, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
//        game.getBoard().addPiece(4, 8, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        game.getBoard().addPiece(3, 4, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

//        game.getBoard().addPiece(4, 4, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

        game.getBoard().printBoard();

        ChessPosition pos = game.getBoard().tempGetKingLoc(ChessGame.TeamColor.BLACK);

        HashSet<ChessMove> moves = (HashSet<ChessMove>) game.validMoves(pos);
        for (ChessMove move : moves) {
            System.out.println(move.toString());
        }
        System.out.println("Check: " + game.isInCheck(ChessGame.TeamColor.BLACK));




    }


    @Test
    @DisplayName("Check if current position is at risk")
    public void testRookCheck() {

    /*  | | | | | | | | |
        | | | |k| | | | |
        | | | | | | |r| |
        | | | |R| | | | |
        | | |R| |R| | | |
        | | | |R| | | | |
        | | | | | | | | |
        | | | | | | | | |   */

        var game = getNewGame();

        game.setBoard(getNewBoard());

        ChessPosition kingPos = getNewPosition(7, 4);



        ChessPiece kingPiece = getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece rookPiece = getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        game.getBoard().removePiece(new ChessPosition(8, 5));
        game.getBoard().addPiece(kingPos, kingPiece);
        game.getBoard().addPiece(new ChessPosition(4, 3), rookPiece);
        game.getBoard().addPiece(new ChessPosition(3, 4), rookPiece);
        game.getBoard().addPiece(new ChessPosition(4, 5), rookPiece);
        game.getBoard().addPiece(new ChessPosition(5, 4), rookPiece);
        game.getBoard().addPiece(new ChessPosition(6,7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        game.getBoard().printBoard();


//        Assertions.assertTrue(game.isInCheckmate(ChessGame.TeamColor.BLACK),
//                "Attack by rook was false.");
        HashSet<ChessMove> moves = (HashSet<ChessMove>) game.validMoves(new ChessPosition(6, 7));
        for (ChessMove move : moves) {
            System.out.println(move.toString());
        }

    }

    // Test the function that evaluates if a knight could attack the king
    @Test
    @DisplayName("Check if current position is at risk")
    public void testKnightCheckOne() {

    /*  | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | |k| | | | |
        | | | | | |N| | |
        | | | | | | | | |
        | | | | | | | | |   */

        ChessPosition kingPos = getNewPosition(4, 4);
        ChessPosition knightPos = getNewPosition(3, 6);

        ChessPiece kingPiece = getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece knightPiece = getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);


        var board = getNewBoard();
        board.addPiece(kingPos, kingPiece);
        board.addPiece(knightPos, knightPiece);


        Assertions.assertTrue(KingCheckEvaluator.positionIsInRisk(board, kingPos),
                "Attack by knight was false.");

    }

    @Test
    @DisplayName("Knight and king, both same color")
    public void testKnightCheckTwo() {

    /*  | | | | | | | | |
        | | | | | | | | |
        | | |N| | | | | |
        | | | | | | | | |
        | | | |K| | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |   */

        ChessPosition kingPos = getNewPosition(4, 4);
        ChessPosition knightPos = getNewPosition(6, 3);

        ChessPiece kingPiece = getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece knightPiece = getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);

        var board = getNewBoard();
        board.addPiece(kingPos, kingPiece);
        board.addPiece(knightPos, knightPiece);


        Assertions.assertFalse(KingCheckEvaluator.positionIsInRisk(board, kingPos),
                "Attack by knight was true, should be false.");

    }

    @Test
    @DisplayName("White pawn in pursuit of black king")
    public void testPawnKingCheckOne() {

    /*  | | | | | | | | |
        | | | |k| | | | |
        | | |P| | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |   */

        ChessPosition kingPos = getNewPosition(7, 4);
        ChessPosition pawnPos = getNewPosition(6, 3);
        ChessPiece kingPiece = getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece pawnPiece = getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        var board = getNewBoard();
        board.addPiece(kingPos, kingPiece);
        board.addPiece(pawnPos, pawnPiece);

        Assertions.assertTrue(KingCheckEvaluator.positionIsInRisk(board, kingPos),
                "Attack by pawn was false, should be true.");
    }

    @Test
    @DisplayName("Black pawn in pursuit of white king")
    public void testPawnKingCheckTwo() {

    /*  | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | | |p| | | |
        | | | |K| | | | |
        | | | | | | | | |   */

        ChessPosition kingPos = getNewPosition(2, 4);
        ChessPosition pawnPos = getNewPosition(3, 5);
        ChessPiece kingPiece = getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece pawnPiece = getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);

        var board = getNewBoard();
        board.addPiece(kingPos, kingPiece);
        board.addPiece(pawnPos, pawnPiece);

        Assertions.assertTrue(KingCheckEvaluator.positionIsInRisk(board, kingPos),
                "Attack by pawn was false, should be true.");
    }


}
