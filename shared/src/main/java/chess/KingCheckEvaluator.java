package chess;


import java.util.HashSet;

// This class is a collection of methods used to determine if a given position is under threat.
// ChessGame will use this to determine an endgame state for when a checkmate or stalemate occurred.
public class KingCheckEvaluator {

    public static boolean positionIsInRisk(ChessBoard board, ChessPosition position) {
        // Determine the team color of the piece at the position of interest
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();

        // First, check if position is at risk of being captured by an enemy knight piece.
        if (isThreatenedByKnight(board, position, pieceColor))
            return true;
        // Check if position can be captured by a pawn.
        else if (isThreatenedByPawn(board, position, pieceColor))
            return true;
        // Check if position can be captured by a rook or queen from an orthogonal direction.
        else if (isAtRiskOrthogonally(board, position, pieceColor))
            return true;
        // Check if position can be captured by a bishop or queen from a diagonal direction.
        else if (isAtRiskDiagonally(board, position, pieceColor))
            return true;

        // King is safe if none of the above boolean methods return true.
        return false;
    }

    // Determine if a position of interest is within the bounds of the chessboard
    private static boolean isInBounds(int row, int col) {
        return (row >= 1 && row < 9 && col >= 1 && col < 9);
    }


    // Check if a position is at risk of being claimed by a knight of the opposing color.
    // There are three conditions that must all be met to return true:
    // 1. The theoretical position of the attacking piece is in bounds
    // 2. The position must contain a knight piece
    // 3. The knight must be of the opposite team color
    private static boolean isThreatenedByKnight(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        // Get location info
        int r = position.getRow();
        int c = position.getColumn();

        // Check for an enemy knight from 8 possible positions.
        if (isInBounds(r + 2, c - 1) && hasEnemyKnight(board, r + 2, c - 1, color)) {
            return true;
        } else if (isInBounds(r + 2, c + 1) && hasEnemyKnight(board, r + 2, c + 1, color)) {
            return true;
        } else if (isInBounds(r + 1, c + 2) && hasEnemyKnight(board, r + 1, c + 2, color)) {
            return true;
        } else if (isInBounds(r - 1, c + 2) && hasEnemyKnight(board, r - 1, c + 2, color)) {
            return true;
        } else if (isInBounds(r - 2, c + 1) && hasEnemyKnight(board, r - 2, c + 1, color)) {
            return true;
        } else if (isInBounds(r - 2, c - 1) && hasEnemyKnight(board, r - 2, c - 1, color)) {
            return true;
        } else if (isInBounds(r - 1, c - 2) && hasEnemyKnight(board, r - 1, c - 2, color)) {
            return true;
        } else if (isInBounds(r + 1, c - 2) && hasEnemyKnight(board, r + 1, c - 2, color)) {
            return true;
        }
        // If none of these spots have an enemy knight, position is not under threat from knight
        return false;
    }

    // This method takes the position of the possible attack piece and the color of the piece at risk.
    // If there is a knight of the opposing color at the given position, returns true.
    private static boolean hasEnemyKnight(ChessBoard board, int r, int c, ChessGame.TeamColor color) {
        return (board.hasPieceAt(r, c)
                && board.getPiece(r, c).getPieceType() == ChessPiece.PieceType.KNIGHT
                && board.getPiece(r, c).getTeamColor() != color);
    }

    // A method whose sole purpose is to check if a position is at risk of being captured by a pawn.
    private static boolean isThreatenedByPawn(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        // Get row/column info
        int r = position.getRow();
        int c = position.getColumn();

        // If the color of the piece potentially at risk is white, check for pawns in the row above.
        // Otherwise, if piece is black, check for pawns below.
        if (color == ChessGame.TeamColor.WHITE) {
            r++; // Increment row value to check row above
        }
        else {
            r--; // If piece is black, decrement row to check below
        }

        // With our new row value, check diagonally left
        if (isInBounds(r, c - 1) && hasEnemyPawn(board, r, c - 1, color)) {
            return true;
        }
        // Check diagonally right
        else if (isInBounds(r, c + 1) && hasEnemyPawn(board, r, c + 1, color)) {
            return true;
        }
        // Making it here means no checked positions contain an enemy pawn, so return false.
        return false;
    }

    // Helper method to determine if enemy pawn is in a given row and column. Must be given a valid location.
    // If the given location has a pawn of the opposite color, returns true.
    private static boolean hasEnemyPawn(ChessBoard board, int r, int c, ChessGame.TeamColor color) {
        return (board.hasPieceAt(r, c)
                && board.getPiece(r, c).getPieceType() == ChessPiece.PieceType.PAWN
                && board.getPiece(r, c).getTeamColor() != color);
    }


    // Temporarily treat the king like a rook to evaluate if the king is in sight of a rook or queen.
    // The "end positions" of the king's moves as a rook will actually be potential start positions
    // of possible rival pieces that can move to the king's position.
    private static boolean isAtRiskOrthogonally(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {

        // Treat the king's current position as an end position for a rook.
        // This gives us all the spaces that a rook (or queen) would need to be in to put the king in check.
        HashSet<ChessMove> orthogonalMoves = PieceMovesCalculator.getRookMoves(board, position);

        // Now iterate through the moves and check if any of the new positions contain a rook or queen.
        for (ChessMove move : orthogonalMoves) {
            // Get row and column of position
            int r = move.getEndPosition().getRow();
            int c = move.getEndPosition().getColumn();

            // Check if it contains a threat (in this case, a rook or queen)
            if (hasEnemyRook(board, r, c, color) || hasEnemyQueen(board, r, c, color)) {
                return true;
            }
        }
        // If all moves are evaluated and none contain a rival piece, the king is safe from orthogonal spaces.
        return false;
    }

    // Temporarily treat the king like a bishop to evaluate if the king is in sight of a bishop or queen.
    // The "end positions" of the king's moves as a bishop will actually be potential start positions
    // of possible rival pieces that can move to the king's position.
    private static boolean isAtRiskDiagonally(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {

        // Get spaces that a bishop (or queen) would need to be in to put the king in check.
        HashSet<ChessMove> diagonalMoves = PieceMovesCalculator.getBishopMoves(board, position);

        // Check all found positions to see if it contains a rival bishop or queen.
        for (ChessMove move : diagonalMoves) {
            // Get row and column of position
            int r = move.getEndPosition().getRow();
            int c = move.getEndPosition().getColumn();

            // Check if it contains a threat (in this case, a rook or queen)
            if (hasEnemyBishop(board, r, c, color) || hasEnemyQueen(board, r, c, color)) {
                return true;
            }
        }
        // If none of the positions has a bishop or queen, king is safe.
        return false;
    }

    // Check if a given position on a board contains a piece, see if it's a rook, then check team color.
    private static boolean hasEnemyRook(ChessBoard board, int r, int c, ChessGame.TeamColor color) {
        return (board.hasPieceAt(r, c)
                && board.getPiece(r, c).getPieceType() == ChessPiece.PieceType.ROOK
                && board.getPiece(r, c).getTeamColor() != color);
    }

    // Check if a given position has a rival bishop piece.
    private static boolean hasEnemyBishop(ChessBoard board, int r, int c, ChessGame.TeamColor color) {
        return (board.hasPieceAt(r, c)
                && board.getPiece(r, c).getPieceType() == ChessPiece.PieceType.BISHOP
                && board.getPiece(r, c).getTeamColor() != color);
    }

    // Check if a given position has a rival queen piece.
    private static boolean hasEnemyQueen(ChessBoard board, int r, int c, ChessGame.TeamColor color) {
        return (board.hasPieceAt(r, c)
                && board.getPiece(r, c).getPieceType() == ChessPiece.PieceType.QUEEN
                && board.getPiece(r, c).getTeamColor() != color);
    }

}
