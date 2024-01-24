package chess;

import javax.imageio.stream.ImageOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;


public class PieceMovesCalculator {


    // Verify that a given row and column is in bounds. Zero indexed.
    private static boolean isInBounds(int row, int column) {
        return (row < 9 && row >= 1 && column < 9 && column >= 1);
    }
    // Same function, but for ChessPosition parameter. One indexed.
    private static boolean isInBounds(ChessPosition pos) {
        return (pos.getRow() < 9 && pos.getRow() >= 1 && pos.getColumn() < 9 && pos.getColumn() >= 1);
    }

    // Verify that a given location is a valid spot to move a piece.
    // First checks if the given location is null (empty). If so, returns true immediately
    // Otherwise, if there is a piece, checks if the piece is a different color.
    private static boolean isGoodSpot(int row, int column, ChessBoard board, ChessGame.TeamColor sameColor) {
        return (board.getPiece(row, column) == null || board.getPiece(row, column).getTeamColor() != sameColor);
    }
    // Same function, but for ChessPosition parameter
    private static boolean isGoodSpot(ChessPosition pos, ChessBoard board, ChessGame.TeamColor sameColor) {
        return (board.getPiece(pos) == null || board.getPiece(pos).getTeamColor() != sameColor);
    }



    // This function is a hub that determines the piece type and calls the corresponding helper function.
    // It returns an Array List of ChessMove objects that gets returned to the pieceMoves function call
    // in the ChessPiece class.
    public static HashSet<ChessMove> calcMoves(ChessBoard board, ChessPosition myPosition) {

        HashSet<ChessMove> moves = new HashSet<>();

        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        switch (type) {

            // For a pawn, check which team its on.
            case PAWN:
                if (color == ChessGame.TeamColor.BLACK) {
                    moves = pawnMovesBlack(board, myPosition); //FIXME
                }
                else {
                    moves = pawnMovesWhite(board, myPosition); //FIXME
                }
                break;

            case ROOK:
                moves = rookMoves(board, myPosition);
                break;

            case KNIGHT:
                moves = knightMoves(board, myPosition);
                break;

            case BISHOP:
                moves = bishopMoves(board, myPosition);
                break;

            case KING:
                moves = kingMoves(board, myPosition);
                break;

            case QUEEN: // The queen has a combination of the rook and bishop moves.
                moves = rookMoves(board, myPosition);
                moves.addAll(bishopMoves(board, myPosition));

                break;

        }

        return moves;

    }

    // KNIGHT PIECE MOVE CALCULATOR
    // Checks eight different squares in L-shape displacements from origin position.
    // If a valid position is found, make a new position object for the end position and
    // then make a new Chess move object.
    public static HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition myPos) {

        HashSet<ChessMove> moves = new HashSet<>();

        // This position object will be changed to try different end move scenarios
        ChessPosition endPos = new ChessPosition(myPos.getRow(), myPos.getColumn());

        // Keep current row, column, and color info
        int r = myPos.getRow();
        int c = myPos.getColumn();
        ChessGame.TeamColor pieceColor = board.getPiece(myPos).getTeamColor();


        // One row down, two columns left
        endPos.setPositionRowColumn(r - 1, c - 2);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            // Add to list. Do NOT use endPos, otherwise all moves will be duplicates from reference.
            // This statement creates both a new position object and new move object to add to list.
            moves.add(new ChessMove(myPos, new ChessPosition(r - 1, c - 2)));
        }

        // One row up, two columns left
        endPos.setPositionRowColumn(r + 1, c - 2);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            moves.add(new ChessMove(myPos, new ChessPosition(r + 1, c - 2)));
        }

        // Two rows up, one column left
        endPos.setPositionRowColumn(r + 2, c - 1);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            moves.add(new ChessMove(myPos, new ChessPosition(r + 2, c - 1)));
        }
        // Two rows up, one column right
        endPos.setPositionRowColumn(r + 2, c + 1);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            moves.add(new ChessMove(myPos, new ChessPosition(r + 2, c + 1)));
        }
        // One row up, two columns right
        endPos.setPositionRowColumn(r + 1, c + 2);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            moves.add(new ChessMove(myPos, new ChessPosition(r + 1, c + 2)));
        }
        // One row down, two columns right
        endPos.setPositionRowColumn(r - 1, c + 2);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            moves.add(new ChessMove(myPos, new ChessPosition(r - 1, c + 2)));
        }
        // Two rows down, one column right
        endPos.setPositionRowColumn(r - 2, c + 1);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            moves.add(new ChessMove(myPos, new ChessPosition(r - 2, c + 1)));
        }
        // Two rows down, one column left
        endPos.setPositionRowColumn(r - 2, c - 1);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            moves.add(new ChessMove(myPos, new ChessPosition(r - 2, c - 1)));
        }

        return moves;
    }

    // BLACK PAWN PIECE MOVE CALCULATOR
    // Checks up to four different squares in front of origin position. Includes check for promotion
    // Checks one space ahead
    // Two spaces ahead (If no moves made yet)
    // One forward, one left (Capture only)
    // One forward, one right (Capture only)
    // Black pawns start in row 7, moving towards 1. White pawns start in row 2, moving towards 8.
    public static HashSet<ChessMove> pawnMovesBlack(ChessBoard board, ChessPosition myPos) {
        HashSet<ChessMove> moves = new HashSet<>();

        int r = myPos.getRow();     // Current row
        int c = myPos.getColumn();  // Current column



        // If the pawn hasn't reached its last row and the space in front is empty
        if (r > 1 && board.getPiece(r - 1, c) == null) {
            ChessPosition endPos = new ChessPosition(r - 1, c);

            // If pawn is about to move to last row, add promotion moves
            if (r == 2) {

                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.QUEEN));
            }
            else { // Otherwise, just add the move without a promotion piece.
                moves.add(new ChessMove(myPos, endPos));
            }


        }
        // If pawn hasn't moved yet and there is nothing within two spaces ahead
        if (r == 7 && board.getPiece(r - 1, c) == null
            && board.getPiece(r - 2, c) == null) {

            ChessPosition endPos = new ChessPosition(r - 2, c);
            moves.add(new ChessMove(myPos, endPos));
        }

        // If there are enemy pieces diagonally to either side, those are potential capture moves
        // Must check if potential capture locations are in bounds first!
        if (isInBounds(r - 1, c - 1) && board.getPiece(r - 1, c - 1) != null &&
            board.getPiece(r - 1, c - 1).getTeamColor() == ChessGame.TeamColor.WHITE) {

            ChessPosition endPos = new ChessPosition(r - 1, c - 1);

            // It is possible to make a capture and promotion simultaneously. Check for promotion
            if (r == 2) {
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.QUEEN));
            }
            else {
                moves.add(new ChessMove(myPos, endPos)); // Add this move to the collection of possible moves
            }
        }
        // Now check the other side for potential capture move
        if (isInBounds(r - 1, c + 1) && board.getPiece(r - 1, c + 1) != null &&
            board.getPiece(r - 1, c + 1).getTeamColor() == ChessGame.TeamColor.WHITE) {

            ChessPosition endPos = new ChessPosition(r - 1, c + 1);

            if (r == 2) {
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.QUEEN));
            }
            else {
                moves.add(new ChessMove(myPos, endPos)); // Add this move to the collection of possible moves
            }
        }


        return moves;
    }

    // WHITE PAWN MOVE CALCULATOR
    // Exactly the same as black, but in opposite direction.
    public static HashSet<ChessMove> pawnMovesWhite(ChessBoard board, ChessPosition myPos) {
        HashSet<ChessMove> moves = new HashSet<>();

        int r = myPos.getRow();
        int c = myPos.getColumn();

        if (r < 8 && board.getPiece(r + 1, c) == null) {
            ChessPosition endPos = new ChessPosition(r + 1, c);

            // Check if pawn is about to reach its last row
            if (r == 7) {
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.QUEEN));
            }
            else {
                moves.add(new ChessMove(myPos, endPos)); // Add this move to the collection of possible moves
            }
        }
        // Check for double forward move (Only possible for first move)
        if (r == 2 && board.getPiece(r + 1, c) == null
            && board.getPiece(r + 2, c) == null) {

            ChessPosition endPos = new ChessPosition(r + 2, c);
            ChessMove move = new ChessMove(myPos, endPos);
            moves.add(move);
        }
        // If there are enemy pieces diagonally to either side, those are potential capture moves
        if (isInBounds(r + 1, c - 1) && board.getPiece(r + 1, c - 1) != null &&
            board.getPiece(r + 1, c - 1).getTeamColor() == ChessGame.TeamColor.BLACK) {

            ChessPosition endPos = new ChessPosition(r + 1, c - 1);

            if (r == 7) {
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.QUEEN));
            }
            else {
                moves.add(new ChessMove(myPos, endPos));
            }
        }
        // Check other side for enemy to capture
        if (isInBounds(r + 1, c + 1) && board.getPiece(r + 1, c + 1) != null &&
            board.getPiece(r + 1, c + 1).getTeamColor() == ChessGame.TeamColor.BLACK) {

            ChessPosition endPos = new ChessPosition(r + 1, c + 1);

            if (r == 7) {
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPos, endPos, ChessPiece.PieceType.QUEEN));
            }
            else {
                moves.add(new ChessMove(myPos, endPos));
            }
        }

        return moves;
    }


    // ROOK PIECE MOVE CALCULATOR
    // Keep checking spaces orthogonally until...
    // Rook bumps into same-color piece
    // Rook reaches edge of board
    // Rook reaches an opponent piece to capture.
    // All empty spaces found are valid moves, or first enemy piece to be in path.
    public static HashSet<ChessMove> rookMoves(ChessBoard board, ChessPosition myPos) {
        HashSet<ChessMove> moves = new HashSet<>();

        // Record current location and team info to determine valid spaces
        int r = myPos.getRow();
        int c = myPos.getColumn();
        ChessGame.TeamColor pieceColor = board.getPiece(myPos).getTeamColor();

        // Spaces are checked using four loops: up, down, left, and right.
        // Check upward until a piece is in the way or edge is reached
        for (int newRow = r + 1; newRow < 9; newRow++) {
            if (!isInBounds(newRow, c)) {
                break; // There are no spots available above. Break
            }
            if (board.getPiece(newRow, c) == null) {
                ChessPosition endPos = new ChessPosition(newRow, c);
                moves.add(new ChessMove(myPos, endPos)); // Add all empty spaces
            }
            else { // If we found a space that isn't null, check if it's an enemy piece.
                if (isGoodSpot(newRow, c, board, pieceColor)) {
                    ChessPosition endPos = new ChessPosition(newRow, c);
                    moves.add(new ChessMove(myPos, endPos));
                }
                break;
            }
        }


        // Check downward until a piece is in the way or edge is reached
        for (int newRow = r - 1; newRow >= 1; newRow--) {
            if (!isInBounds(newRow, c)) {
                break; // No spots below
            }
            if (board.getPiece(newRow, c) == null) {
                ChessPosition endPos = new ChessPosition(newRow, c);
                moves.add(new ChessMove(myPos, endPos)); // Add all empty spaces
            }
            else { // If we found a space that isn't null, check if it's an enemy piece.
                if (isGoodSpot(newRow, c, board, pieceColor)) {
                    ChessPosition endPos = new ChessPosition(newRow, c);
                    moves.add(new ChessMove(myPos, endPos));
                }
                break;
            }
        }

        // Check left until a piece is in the way or edge is reached
        for (int newCol = c - 1; newCol >= 1; newCol--) {
            if (!isInBounds(r, newCol)) {
                break; // No spots available leftward
            }
            if (board.getPiece(r, newCol) == null) {
                ChessPosition endPos = new ChessPosition(r, newCol);
                moves.add(new ChessMove(myPos, endPos)); // Add all empty spaces
            }
            else { // If we found a space that isn't null, check if it's an enemy piece.
                if (isGoodSpot(r, newCol, board, pieceColor)) {
                    ChessPosition endPos = new ChessPosition(r, newCol);
                    moves.add(new ChessMove(myPos, endPos));
                }
                break;
            }
        }

        // Check right until a piece is in the way or edge is reached
        for (int newCol = c + 1; newCol < 9; newCol++) {
            if (!isInBounds(r, newCol)) {
                break; // No spots available rightward
            }
            if (board.getPiece(r, newCol) == null) {
                ChessPosition endPos = new ChessPosition(r, newCol);
                moves.add(new ChessMove(myPos, endPos)); // Add all empty spaces
            }
            else { // If we found a space that isn't null, check if it's an enemy piece.
                if (isGoodSpot(r, newCol, board, pieceColor)) {
                    ChessPosition endPos = new ChessPosition(r, newCol);
                    moves.add(new ChessMove(myPos, endPos));
                }
                break;
            }
        }


        return moves;
    }

    // BISHOP MOVES
    // A bishop checks spaces for conditions just like what the rook looks for, but diagonally.
    // Cannot move orthogonally.
    public static HashSet<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPos) {
        HashSet<ChessMove> moves = new HashSet<>();

        // Save current row/column in variables for easier lookups
        int r = myPos.getRow();
        int c = myPos.getColumn();
        ChessGame.TeamColor pieceColor = board.getPiece(myPos).getTeamColor();


        // Spaces for bishop are checked with four main loops: up-left, up-right, down-right, down-left.
        // Checking up and left
        for (int newRow = r + 1, newCol = c - 1; newRow < 9 && newCol > 0;  newRow++, newCol--) {
            if (!isInBounds(newRow, newCol)) {
                break; // There are no spots available above. Break
            }
            if (board.getPiece(newRow, newCol) == null) {
                ChessPosition endPos = new ChessPosition(newRow, newCol);
                moves.add(new ChessMove(myPos, endPos)); // Add all empty spaces
            }
            else { // If we found a space that isn't null, check if it's an enemy piece.
                if (isGoodSpot(newRow, newCol, board, pieceColor)) {
                    ChessPosition endPos = new ChessPosition(newRow, newCol);
                    moves.add(new ChessMove(myPos, endPos));
                }
                break;
            }
        }


        // Check up and right
        for (int newRow = r + 1, newCol = c + 1; newRow < 9 && newCol < 9;  newRow++, newCol++) {
            if (!isInBounds(newRow, newCol)) {
                break; // No spots below
            }
            if (board.getPiece(newRow, newCol) == null) {
                ChessPosition endPos = new ChessPosition(newRow, newCol);
                moves.add(new ChessMove(myPos, endPos)); // Add all empty spaces
            }
            else { // If we found a space that isn't null, check if it's an enemy piece.
                if (isGoodSpot(newRow, newCol, board, pieceColor)) {
                    ChessPosition endPos = new ChessPosition(newRow, newCol);
                    moves.add(new ChessMove(myPos, endPos));
                }
                break;
            }
        }

        // Check down and right
        for (int newRow = r - 1, newCol = c + 1; newRow > 0 && newCol < 9;  newRow--, newCol++) {
            if (!isInBounds(newRow, newCol)) {
                break; // No spots available leftward
            }
            if (board.getPiece(newRow, newCol) == null) {
                ChessPosition endPos = new ChessPosition(newRow, newCol);
                moves.add(new ChessMove(myPos, endPos)); // Add all empty spaces
            }
            else { // If we found a space that isn't null, check if it's an enemy piece.
                if (isGoodSpot(newRow, newCol, board, pieceColor)) {
                    ChessPosition endPos = new ChessPosition(newRow, newCol);
                    moves.add(new ChessMove(myPos, endPos));
                }
                break;
            }
        }

        // Check down and left
        for (int newRow = r - 1, newCol = c - 1; newRow > 0 && newCol > 0;  newRow--, newCol--) {
            if (!isInBounds(newRow, newCol)) {
                break; // No spots available rightward
            }
            if (board.getPiece(newRow, newCol) == null) {
                ChessPosition endPos = new ChessPosition(newRow, newCol);
                moves.add(new ChessMove(myPos, endPos)); // Add all empty spaces
            }
            else { // If we found a space that isn't null, check if it's an enemy piece.
                if (isGoodSpot(newRow, newCol, board, pieceColor)) {
                    ChessPosition endPos = new ChessPosition(newRow, newCol);
                    moves.add(new ChessMove(myPos, endPos));
                }
                break;
            }
        }
        return moves;
    }

    // KING PIECE MOVES
    // A king piece can move one space into any of the eight spots around it, as long as they are not
    // occupied by another piece of the same color.
    public static HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition myPos) {
        HashSet<ChessMove> moves = new HashSet<>();

        // Use this as a candidate end position object
        ChessPosition endPos = new ChessPosition(myPos.getRow(), myPos.getColumn());
        int r = myPos.getRow();
        int c = myPos.getColumn();
        ChessGame.TeamColor pieceColor = board.getPiece(myPos).getTeamColor();


        // Test space directly to left
        endPos.setPositionRowColumn(r, c - 1);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            ChessMove move = new ChessMove(myPos, new ChessPosition(r, c - 1));
            moves.add(move);
        }

        // Test space left and up
        endPos.setPositionRowColumn(r + 1, c - 1);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            ChessMove move = new ChessMove(myPos, new ChessPosition(r + 1, c - 1));
            moves.add(move);
        }

        // Test space up
        endPos.setPositionRowColumn(r + 1, c);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            ChessMove move = new ChessMove(myPos, new ChessPosition(r + 1, c));
            moves.add(move);
        }

        // Test space up and right
        endPos.setPositionRowColumn(r + 1, c + 1);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            ChessMove move = new ChessMove(myPos, new ChessPosition(r + 1, c + 1));
            moves.add(move);
        }

        // Test space directly to right
        endPos.setPositionRowColumn(r, c + 1);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            ChessMove move = new ChessMove(myPos, new ChessPosition(r, c + 1));
            moves.add(move);
        }

        // Test position right and down
        endPos.setPositionRowColumn(r - 1, c + 1);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            ChessMove move = new ChessMove(myPos, new ChessPosition(r - 1, c + 1));
            moves.add(move);
        }

        // Test position directly downward
        endPos.setPositionRowColumn(r - 1, c);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            ChessMove move = new ChessMove(myPos, new ChessPosition(r - 1, c));
            moves.add(move);
        }

        // Test position down and left
        endPos.setPositionRowColumn(r - 1, c - 1);
        if (isInBounds(endPos) && isGoodSpot(endPos, board, pieceColor)) {
            ChessMove move = new ChessMove(myPos, new ChessPosition(r - 1, c - 1));
            moves.add(move);
        }


        return moves;
    }

    // NOTE ON QUEEN MOVES
    // The queen does not need a separate helper function, as the queen piece's moves are
    // simply a combination of the rook and bishop piece possible moves.

}
