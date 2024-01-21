package chess;



import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import chess.KnightMovesCalculator;


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    // Private Member variables for this class
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    private boolean hasMoved; // Useful for pawn moves

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }



    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        // TODO: Declare a temporary variable, Collection<ChessMove> to fill



        // TODO: Use if/else logic to determine piece.
        // Logic for each piece

        // PAWN:
        // Check if in initial position
        // Available moves include the following:
        // One spot forward
        // Two spots forward
        // Forward one, left one if occupied by opposing piece
        // Forward one, right one if occupied by opposing piece

        // ROOK:



        // BISHOP:

        // KING:

        // QUEEN:


        throw new RuntimeException("Not implemented");
    }





    // TODO: Fill in these helper methods
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPos) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        //FIXME: Need to add more logic
        return moves;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

}
