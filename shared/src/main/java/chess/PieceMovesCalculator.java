package chess;

import javax.imageio.stream.ImageOutputStream;
import java.util.ArrayList;
import java.util.Collection;


public class PieceMovesCalculator {



    // Easy validation function to make sure a position is not out of bounds
    // or occupied by another chess piece of the same team color.
    private static boolean isValidKnightPosition(int row, int col, ChessBoard board) {
        // Get position and team color info
        ChessPosition pos = new ChessPosition(row, col);
        ChessGame.TeamColor sameColor = board.getPiece(pos).getTeamColor();

        // Is it within the bounds of the board?
        boolean inBounds = (row < 8 && row >= 0 && col < 8 && col >= 0);
        // Is the space either empty or an enemy piece?
        boolean goodToClaim = (board.getPiece(pos) == null || board.getPiece(pos).getTeamColor() != sameColor);

        return (inBounds && goodToClaim); // If both conditions are met, return true.
    }



    // This function is a hub that determines the piece type and calls the corresponding helper function.
    // It returns an Array List of ChessMove objects that gets returned to the pieceMoves function call
    // in the ChessPiece class.
    public static ArrayList<ChessMove> calcMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        switch (type) {

            // For a pawn, check which team its on.
            case PAWN:
                if (color == ChessGame.TeamColor.BLACK) {
                    moves = pawnMovesBlack(board, myPosition);
                }
                else {
                    moves = pawnMovesWhite(board, myPosition);
                }
            case ROOK:
            //FIXME
            case KNIGHT:
                moves = knightMoves(board, myPosition);

            case BISHOP:
            //FIXME
            case KING:
                //FIXME
            case QUEEN:

        }

        return moves;

    }

    // KNIGHT PIECE MOVE CALCULATOR
    // Checks eight different squares in L-shape displacements from origin position.
    // If a valid position is found, make a new position object for the end position and
    // then make a new Chess move object.
    public static ArrayList<ChessMove> knightMoves(ChessBoard board, ChessPosition myPos) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        // One row down, two columns left
        if (isValidKnightPosition(myPos.getRow() - 1, myPos.getColumn() - 2, board)) {

            // If the position is valid, store it.
            ChessPosition endPos = new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 2);
            // Make a new ChessMove object.
            ChessMove move =  new ChessMove(myPos, endPos);
            // Add to list.
            moves.add(move);
        }
        // One row up, two columns left
        if (isValidKnightPosition(myPos.getRow() + 1, myPos.getColumn() - 2, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 2);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // Two rows up, one column left
        if (isValidKnightPosition(myPos.getRow() + 2, myPos.getColumn() - 1, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() + 2, myPos.getColumn() - 1);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // Two rows up, one column right
        if (isValidKnightPosition(myPos.getRow() + 2, myPos.getColumn() + 1, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() + 2, myPos.getColumn() + 1);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // One row up, two columns right
        if (isValidKnightPosition(myPos.getRow() + 1, myPos.getColumn() + 2, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 2);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // One row down, two columns right
        if (isValidKnightPosition(myPos.getRow() - 1, myPos.getColumn() + 2, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 2);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // Two rows down, one column right
        if (isValidKnightPosition(myPos.getRow() - 2, myPos.getColumn() + 1, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() - 2, myPos.getColumn() + 1);
            ChessMove move =  new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // Two rows down, one column left
        if (isValidKnightPosition(myPos.getRow() - 2, myPos.getColumn() - 1, board)) {

            ChessPosition endPos = new ChessPosition(myPos.getRow() - 2, myPos.getColumn() - 1);
            ChessMove move = new ChessMove(myPos, endPos);
            moves.add(move);
        }

        return moves;
    }

    // BLACK PAWN PIECE MOVE CALCULATOR
    // Checks up to four different squares in front of origin position. Includes check for promotion
    // Checks one space ahead
    // Two spaces ahead (If no moves made yet)
    // One forward, one left (Capture only)
    // One forward, one right (Capture only)
    // Black pawns start in row 6, moving towards 0. White pawns start in row 1, moving towards 7.
    public static ArrayList<ChessMove> pawnMovesBlack(ChessBoard board, ChessPosition myPos) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        int r = myPos.getRow();     // Current row
        int c = myPos.getColumn();  // Current column


        // If the pawn hasn't reached its last row and the space in front is empty
        if (r > 0 && board.getPiece(r - 1, c) == null) {
            ChessPosition endPos = new ChessPosition(r - 1, c);
            ChessMove move = new ChessMove(myPos, endPos);

            // If pawn is about to move to last row, add promotion moves
            if (r == 1) {
                move.SetPromotionPiece(ChessPiece.PieceType.ROOK);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.KNIGHT);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.BISHOP);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.QUEEN);
            }
            else {
                moves.add(move); // Add this move to the collection of possible moves
            }


        }
        // If pawn hasn't moved yet and there is nothing within two spaces ahead
        if (!board.getPiece(myPos).hasMoved && board.getPiece(r - 1, c) == null
            && board.getPiece(r - 2, c) == null) {

            ChessPosition endPos = new ChessPosition(r - 2, c);
            ChessMove move = new ChessMove(myPos, endPos);
            moves.add(move);
        }

        // If there are enemy pieces diagonally to either side, those are potential capture moves
        if (board.getPiece(r - 1, c - 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition endPos = new ChessPosition(r - 1, c - 1);
            ChessMove move = new ChessMove(myPos, endPos);

            // Also need to check for promotion here
            if (r == 1) {
                move.SetPromotionPiece(ChessPiece.PieceType.ROOK);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.KNIGHT);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.BISHOP);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.QUEEN);
            }
            else {
                moves.add(move); // Add this move to the collection of possible moves
            }
        }

        if (board.getPiece(r - 1, c + 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition endPos = new ChessPosition(r - 1, c + 1);
            ChessMove move = new ChessMove(myPos, endPos);
            if (r == 1) {
                move.SetPromotionPiece(ChessPiece.PieceType.ROOK);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.KNIGHT);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.BISHOP);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.QUEEN);
            }
            else {
                moves.add(move); // Add this move to the collection of possible moves
            }
        }


        return moves;
    }

    // WHITE PAWN MOVE CALCULATOR
    // Exactly the same as black, but in opposite direction.
    public static ArrayList<ChessMove> pawnMovesWhite(ChessBoard board, ChessPosition myPos) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        int r = myPos.getRow();
        int c = myPos.getColumn();

        if (r < 7 && board.getPiece(r + 1, c) == null) {
            ChessPosition endPos = new ChessPosition(r + 1, c);
            ChessMove move = new ChessMove(myPos, endPos);

            // Check if pawn is about to reach its last row
            if (r == 6) {
                move.SetPromotionPiece(ChessPiece.PieceType.ROOK);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.KNIGHT);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.BISHOP);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.QUEEN);
            }
            else {
                moves.add(move); // Add this move to the collection of possible moves
            }


        }

        if (!board.getPiece(myPos).hasMoved && board.getPiece(r + 1, c) == null
                && board.getPiece(r + 2, c) == null) {

            ChessPosition endPos = new ChessPosition(r + 2, c);
            ChessMove move = new ChessMove(myPos, endPos);
            moves.add(move);
        }

        // If there are enemy pieces diagonally to either side, those are potential capture moves
        if (board.getPiece(r + 1, c - 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
            ChessPosition endPos = new ChessPosition(r + 1, c - 1);
            ChessMove move = new ChessMove(myPos, endPos);

            if (r == 6) {
                move.SetPromotionPiece(ChessPiece.PieceType.ROOK);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.KNIGHT);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.BISHOP);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.QUEEN);
            }
            else {
                moves.add(move);
            }
        }

        if (board.getPiece(r + 1, c + 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
            ChessPosition endPos = new ChessPosition(r + 1, c + 1);
            ChessMove move = new ChessMove(myPos, endPos);

            if (r == 6) {
                move.SetPromotionPiece(ChessPiece.PieceType.ROOK);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.KNIGHT);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.BISHOP);
                moves.add(move);
                move.SetPromotionPiece(ChessPiece.PieceType.QUEEN);
            }
            else {
                moves.add(move);
            }
        }

        return moves;
    }


    // ROOK PIECE MOVE CALCULATOR
    // Keep checking spaces upward until...
    // Rook bumps into same-color piece
    // Rook reaches edge of board
    // Rook reaches an opponent piece to capture.
    // Repeat to the left, right, and downward.
    // All empty spaces found are valid moves, or first enemy piece to be in path.
    public static ArrayList<ChessMove> rookMoves(ChessBoard board, ChessPosition myPos) {
        ArrayList<ChessMove> moves = new ArrayList<>();


        //FIXME: Do some stuff

        return moves;
    }

    // KING PIECE MOVES
    // A king piece can move one space into any of the eight spots around it, as long as they are not
    // occupied by another piece of the same color.
    public static ArrayList<ChessMove> kingMoves(ChessBoard board, ChessPosition myPos) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        int r = myPos.getRow();
        int c = myPos.getColumn();
        ChessGame.TeamColor sameColor = board.getPiece(myPos).getTeamColor();

        if (board.getPiece(r, c - 1).getTeamColor() != sameColor) {
            ChessPosition endPos = new ChessPosition(r, c - 1);
            ChessMove move = new ChessMove(myPos, endPos);
            moves.add(move);
        }

        if (board.getPiece(r + 1, c - 1).getTeamColor() != sameColor) {
            ChessPosition endPos = new ChessPosition(r + 1, c - 1);
            ChessMove move = new ChessMove(myPos, endPos);

            moves.add(move);
        }

        if (board.getPiece(r + 1, c).getTeamColor() != sameColor) {
            ChessPosition endPos = new ChessPosition(r + 1, c);
            ChessMove move = new ChessMove(myPos, endPos);
            moves.add(move);
        }

        if (board.getPiece(r + 1, c + 1).getTeamColor() != sameColor) {
            ChessPosition endPos = new ChessPosition(r + 1, c + 1);
            ChessMove move = new ChessMove(myPos, endPos);
            moves.add(move);
        }

        if (board.getPiece(r, c + 1).getTeamColor() != sameColor) {
            ChessPosition endPos = new ChessPosition(r, c + 1);
            ChessMove move = new ChessMove(myPos, endPos);
            moves.add(move);
        }

        if (board.getPiece(r - 1, c + 1).getTeamColor() != sameColor) {
            ChessPosition endPos = new ChessPosition(r - 1, c + 1);
            ChessMove move = new ChessMove(myPos, endPos);
            moves.add(move);
        }

        if (board.getPiece(r - 1, c).getTeamColor() != sameColor) {
            ChessPosition endPos = new ChessPosition(r - 1, c);
            ChessMove move = new ChessMove(myPos, endPos);
            moves.add(move);
        }

        if (board.getPiece(r - 1, c - 1).getTeamColor() != sameColor) {
            ChessPosition endPos = new ChessPosition(r - 1, c - 1);
            ChessMove move = new ChessMove(myPos, endPos);
            moves.add(move);
        }


        return moves;
    }

}
