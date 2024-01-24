
//FIXME: Change indexing to 1-indexed

package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {
        squares = new ChessPiece[8][8];
//        resetBoard();
    }

    // This is a 2D array of ChessPiece objects.
    // Their Piecetypes either hold a game piece or are set to null.
    private ChessPiece[][] squares;


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int r = position.getRow() - 1;
        int c = position.getColumn() - 1;

        squares[r][c] = piece;


    }

    // Overload function that takes row and column info as parameters
    public void addPiece(int row, int col, ChessPiece piece) {

            squares[row - 1][col - 1] = piece;
    }

    // Helper function for removing a piece from the board by setting it to null.
    // Called when a piece is captured.
    public void removePiece(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    } // TODO:



    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    // Overloaded function if user gives row and column parameters instead.
    public ChessPiece getPiece(int row, int column) {
        return squares[row - 1][column - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        // Make temporary piece variables to add later
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);

        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

        // Clear all pieces and empty board
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                squares[r][c] = null;
            }
        }

        // Add all WHITE PAWNS to board
        for (int c = 1; c < 9; c++) {
            addPiece(2, c, whitePawn);
        }
        // Add remaining white pieces to board
        addPiece(1, 1, whiteRook);
        addPiece(1, 2, whiteKnight);
        addPiece(1, 3, whiteBishop);
        addPiece(1, 4, whiteQueen);
        addPiece(1, 5, whiteKing);
        addPiece(1, 6, whiteBishop);
        addPiece(1, 7, whiteKnight);
        addPiece(1, 8, whiteRook);

        // Add all BLACK PAWNS to board
        for (int c = 1; c < 9; c++) {
            addPiece(7, c, blackPawn);
        }
        // Remaining black pieces to board
        addPiece(8, 1, blackRook);
        addPiece(8, 2, blackKnight);
        addPiece(8, 3, blackBishop);
        addPiece(8, 4, blackQueen);
        addPiece(8, 5, blackKing);
        addPiece(8, 6, blackBishop);
        addPiece(8, 7, blackKnight);
        addPiece(8, 8, blackRook);
    }

    @Override
    public boolean equals(Object o) {
        if (this == null && o == null) return true;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }
}
