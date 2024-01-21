package chess;

import java.util.ArrayList;
import java.util.Collection;

import chess.ChessPiece;

// THE KNIGHT MOVES CALCULATOR
// Knight pieces move in L-shapes. There are 8 maximum possible positions a knight can move to.
public class KnightMovesCalculator {



    // Easy validation function to make sure a position is not out of bounds
    // or occupied by another chess piece of the same team color.
    protected boolean isValidPosition(int row, int col, ChessBoard board) {
        // Get position and team color info
        ChessPosition pos = new ChessPosition(row, col);
        ChessGame.TeamColor sameColor = board.getPiece(pos).getTeamColor();

        return (row < 8 && row >= 0 && col < 8 && col >= 0
                && board.getPiece(pos).getTeamColor() != sameColor);
    }

    // KNIGHT PIECE MOVE CALCULATOR
    // Checks eight different squares in L-shape displacements from origin position.
    // If a valid position is found, make a new position object for the end position and
    // then make a new Chess move object.
    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPos) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        // One row down, two columns left
        if (isValidPosition(myPos.getRow() - 1, myPos.getColumn() - 2, board)) {

            // If the position is valid, store it.
            ChessPosition endPos = new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 2);
            // Make a new ChessMove object.
            ChessMove move =  new ChessMove(myPos, endPos);
            // Add to list.
            moves.add(move);
        }
        // One row up, two columns left
        if (isValidPosition(myPos.getRow() + 1, myPos.getColumn() - 2, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 2);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // Two rows up, one column left
        if (isValidPosition(myPos.getRow() + 2, myPos.getColumn() - 1, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() + 2, myPos.getColumn() - 1);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // Two rows up, one column right
        if (isValidPosition(myPos.getRow() + 2, myPos.getColumn() + 1, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() + 2, myPos.getColumn() + 1);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // One row up, two columns right
        if (isValidPosition(myPos.getRow() + 1, myPos.getColumn() + 2, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 2);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // One row down, two columns right
        if (isValidPosition(myPos.getRow() - 1, myPos.getColumn() + 2, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 2);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // Two rows down, one column right
        if (isValidPosition(myPos.getRow() - 2, myPos.getColumn() + 1, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() - 2, myPos.getColumn() + 1);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // Two rows down, one column left
        if (isValidPosition(myPos.getRow() - 2, myPos.getColumn() - 1, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() - 2, myPos.getColumn() - 1);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }

        return moves;
    }






}
