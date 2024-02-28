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
    @DisplayName("Check print board")
    public void testCasesFunc() {

    /*  |r|n|b|q|k|b|n|r|
        |p|p|p|p|p|p|p|p|
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        |P|P|P|P|P|P|P|P|
        |R|N|B|Q|K|B|N|R|   */


        var game = getNewGame();


        ChessBoardPrint.printChessBoard(game.getBoard());

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
