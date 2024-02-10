package chess;

/**
 * ChessBoardPrint.java created by Isaac Gubelin on February 9, 2024
 * <p>
 *     This class contains a method that prints the layout of a given chessboard.
 * </p>
 */
public class ChessBoardPrint {

    // Maximum row/column dimension
    private static final int OUT_BOUNDS = 9;

    // Unicode constant chars for printing chess pieces
    private static final char WHITE_KING   = '\u2654';
    private static final char WHITE_QUEEN  = '\u2655';
    private static final char WHITE_ROOK   = '\u2656';
    private static final char WHITE_BISHOP = '\u2657';
    private static final char WHITE_KNIGHT = '\u2658';
    private static final char WHITE_PAWN   = '\u2659';
    private static final char BLACK_KING   = '\u265A';
    private static final char BLACK_QUEEN  = '\u265B';
    private static final char BLACK_ROOK   = '\u265C';
    private static final char BLACK_BISHOP = '\u265D';
    private static final char BLACK_KNIGHT = '\u265E';
    private static final char BLACK_PAWN   = '\u265F';

    /**
     * This method takes in a Chesspiece object and returns a unicode chess char of the same type and color.
     * @param piece The type and color are used to select the correct unicode char
     * @return pieceChar will have the requested unicode character.
     */
    private static char getPieceChar(ChessPiece piece) {
        char pieceChar = ' ';
        ChessGame.TeamColor color = piece.getTeamColor();

        switch (piece.getPieceType()) { // Automatically sets piece character to correct unicode character and color.
            case KING:
                pieceChar = (color == ChessGame.TeamColor.WHITE) ? WHITE_KING : BLACK_KING;
                break;

            case QUEEN:
                pieceChar = (color == ChessGame.TeamColor.WHITE) ? WHITE_QUEEN : BLACK_QUEEN;
                break;

            case BISHOP:
                pieceChar = (color == ChessGame.TeamColor.WHITE) ? WHITE_BISHOP : BLACK_BISHOP;
                break;

            case KNIGHT:
                pieceChar = (color == ChessGame.TeamColor.WHITE) ? WHITE_KNIGHT : BLACK_KNIGHT;
                break;

            case ROOK:
                pieceChar = (color == ChessGame.TeamColor.WHITE) ? WHITE_ROOK : BLACK_ROOK;
                break;

            case PAWN:
                pieceChar = (color == ChessGame.TeamColor.WHITE) ? WHITE_PAWN : BLACK_PAWN;
                break;

            default:
                return '?';
        }
        return pieceChar;
    }

    /**
     * The main printing function to be used as to-string.
     * @param board The board to be printed and displayed
     */
    public static void printChessBoard(ChessBoard board) {

        // Go through every square in the board
        for (int row = 1; row < OUT_BOUNDS; row++) {
            for (int col = 1; col < OUT_BOUNDS; col++) {

                System.out.print('|');                                          // Print square border
                if (board.hasPieceAt(row, col))
                    System.out.print(getPieceChar(board.getPiece(row, col)));   // If there's a piece, print it
                else
                    System.out.print("\u2001");                             // Otherwise, print blank square
            }
            System.out.println('|');                                        // For last column, print right border
        }
    }
}
