package ui;

import static ui.EscapeSequences.*;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

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
    private static final char BLACK_PAWN   = '\u265f';

    private static final String UNICODE_ESCAPE = "\u001b";  // Used for setting background color

    /**
     * This method takes in a Chesspiece object and returns a unicode chess char of the same type and color.
     * @param piece The type and color are used to select the correct unicode char
     * @return pieceChar will have the requested unicode character.
     */
    private static char getPieceEmoji(ChessPiece piece) {
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

    // Get just the letter for the chess piece
    // K is used for king, N used for knight
    private static char getPieceLetter(ChessPiece piece) {
        char pieceChar = ' ';

        switch (piece.getPieceType()) { // Automatically sets piece character to correct unicode character and color.
            case KING:
                pieceChar = 'K';
                break;

            case QUEEN:
                pieceChar = 'Q';
                break;

            case BISHOP:
                pieceChar = 'B';
                break;

            case KNIGHT:
                pieceChar = 'N';
                break;

            case ROOK:
                pieceChar = 'R';
                break;

            case PAWN:
                pieceChar = 'P';
                break;

            default:
                return ' ';
        }
        return pieceChar;
    }


    /**
     * Prints the board with white pieces at bottom, black pieces at top.
     * @param board The board to be printed and displayed
     */
    public static void printChessWhitePerspective(ChessBoard board) {
        // Print top border of board with column labels left to right
        System.out.println(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY +
                "    a  b  c  d  e  f  g  h    " + SET_BG_COLOR_BLACK);
        // Go through every square in the board
        for (int row = 8; row > 0; row--) {     // Go from top to bottom
            // print row number on chessboard border
            System.out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            for (int col = 1; col < OUT_BOUNDS; col++) {
                setBackgroundToSquareColor(row, col);                           // Chess square color
                if (board.hasPieceAt(row, col)) {                               // If there's a piece, print it
                    setTextToPieceColor(board.getPiece(row, col).getTeamColor()); // Print text in correct color
                    System.out.print(" " + getPieceLetter(board.getPiece(row, col)) + " ");
                }
                else
                    System.out.print("   ");                            // Otherwise, print blank square
            }
            // print row number on chessboard right side border
            System.out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            System.out.print(SET_BG_COLOR_BLACK + "\n");
        }
        // Print bottom border of board with column labels left to right
        System.out.println(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY +
                "    a  b  c  d  e  f  g  h    " + SET_BG_COLOR_BLACK);
        System.out.println(RESET_BG_COLOR + SET_TEXT_COLOR_WHITE); // Reset text to usual color
    }

    public static void printChessBlackPerspective(ChessBoard board) {
        // Print the column labels right to left
        System.out.println(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY +
                            "    h  g  f  e  d  c  b  a    " + SET_BG_COLOR_BLACK);

        // Go through every square in the board
        for (int row = 1; row < OUT_BOUNDS; row++) {     // Go from bottom to top
            // print row number on chessboard border
            System.out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            for (int col = 8; col > 0; col--) {
                setBackgroundToSquareColor(row, col);                           // Chess square color
                if (board.hasPieceAt(row, col)) {                               // If there's a piece, print it
                    setTextToPieceColor(board.getPiece(row, col).getTeamColor()); // Print text in correct color
                    System.out.print(" " + getPieceLetter(board.getPiece(row, col)) + " ");
                }
                else
                    System.out.print("   ");                            // Otherwise, print blank square
            }
            System.out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            System.out.print(SET_BG_COLOR_BLACK + "\n");
        }
        // Print bottom border of chess board with column labels
        System.out.println(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY +
                "    h  g  f  e  d  c  b  a    " + SET_BG_COLOR_BLACK);
        System.out.println(RESET_BG_COLOR + SET_TEXT_COLOR_WHITE); // Reset text to usual color
    }

    public static void printChessSpectatorView(ChessBoard board) {
        // Print the column labels right to left
        System.out.println(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY +
                "    1  2  3  4  5  6  7  8    " + SET_BG_COLOR_BLACK);
        char colLetter = 'a'; // Used for column labels (rows for spectator view)

        // Go through every square in the board
        for (int col = 1; col < OUT_BOUNDS; col++) {     // Go from bottom to top
            // print row number on chessboard border
            System.out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + colLetter + " ");
            for (int row = 1; row < OUT_BOUNDS; row++) {
                setBackgroundToSquareColor(row, col);                           // Chess square color
                if (board.hasPieceAt(row, col)) {                               // If there's a piece, print it
                    setTextToPieceColor(board.getPiece(row, col).getTeamColor()); // Print text in correct color
                    System.out.print(" " + getPieceLetter(board.getPiece(row, col)) + " ");
                }
                else
                    System.out.print("   ");                            // Otherwise, print blank square
            }
            System.out.print(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY + " " + colLetter + " ");
            System.out.print(SET_BG_COLOR_BLACK + "\n");
            colLetter++; // Changes label to next letter for next printout
        }
        // Print bottom border of chess board with column labels
        System.out.println(SET_TEXT_COLOR_DARK_GREY + SET_BG_COLOR_LIGHT_GREY +
                "    1  2  3  4  5  6  7  8    " + SET_BG_COLOR_BLACK);
        System.out.println(RESET_BG_COLOR + SET_TEXT_COLOR_WHITE); // Reset text to usual color
    }



    private static void setTextToPieceColor(ChessGame.TeamColor color) {
        if (color.equals(ChessGame.TeamColor.BLACK))
            System.out.print(SET_TEXT_COLOR_BLACK + SET_TEXT_BOLD);
        else
            System.out.print(SET_TEXT_COLOR_WHITE + SET_TEXT_BOLD);
    }

    private static void setBackgroundToSquareColor(int row, int col) {
        if ((row + col) % 2 == 0)                     // Check if square is odd or even for choosing color
            System.out.print(UNICODE_ESCAPE + "[48;5;222m");    // Creme yellow
        else
            System.out.print(UNICODE_ESCAPE + "[48;5;95m");     // Brown
    }

}
