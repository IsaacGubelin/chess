package chess;


// This class is a collection of methods used to determine if a given position is under threat.
// ChessGame will use this to determine an endgame state for when a checkmate or stalemate occurred.
public class KingCheckEvaluator {

    public static boolean positionIsInRisk(ChessBoard board, ChessPosition position) {
        // Determine the team color of the piece at the position of interest
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();




        return true; // FIXME this ain't right

    }

    // Determine if a position of interest is within the bounds of the chessboard
    private static boolean isInBounds(int row, int col) {
        return (row >= 1 && row < 9 && col >= 1 && col < 9);
    }


    // Check if a position is at risk of being claimed by a knight of the opposing color.
    // There are three conditions that must be met for this to return true:
    // 1. The theoretical position of the attacking piece is in bounds
    // 2. The position must contain a knight
    // 3.
    private static boolean isThreatenedByKnight(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        // Get location info
        int r = position.getRow();
        int c = position.getColumn();


        if (isInBounds(r + 2, c - 1) && hasEnemyKnight(board, r + 2, c - 1, color)) {
            return true;
        } else if (isInBounds(r + 2, c + 1) && hasEnemyKnight(board, r + 2, c + 1, color)) {
            return true;
        } else if (isInBounds(r + 1, c + 2) && hasEnemyKnight(board, r + 1, c + 2, color)) {
            return true;
        }

        //FIXME finish this thing
        return false;
    }

    // This method takes the position of the possible attack piece and the color of the piece at risk.
    // If there is a knight of the opposing color at the given position, returns true.
    private static boolean hasEnemyKnight(ChessBoard board, int r, int c, ChessGame.TeamColor color) {
        return (board.hasPieceAt(r, c)
                && board.getPiece(r, c).getPieceType() == ChessPiece.PieceType.KNIGHT
                && board.getPiece(r, c).getTeamColor() != color);
    }

}
