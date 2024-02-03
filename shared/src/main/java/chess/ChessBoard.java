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

        // Initialize the squares (chess board)
        squares = new ChessPiece[8][8];
        
    }

    private ChessPiece[][] squares; // A 2D array for chess pieces. Empty locations are set to null.

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public void addPiece(int row, int column, ChessPiece piece) {
        squares[row - 1][column - 1] = piece;
    }

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

    public ChessPiece getPiece(int row, int column) {
        return squares[row - 1][column - 1];
    }

    /**
     * A function to set a board position to a specified Chess piece type and color
     *
     * @param position
     * @param color
     * @param type
     */
    public void setPiece(ChessPosition position, ChessGame.TeamColor color, ChessPiece.PieceType type) {
        squares[position.getRow() - 1][position.getColumn() - 1] = new ChessPiece(color, type);
    }

    /**
     * Remove a piece from the board by setting it to null
     *
     */
    public void removePiece(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    // Function to check if a square is null. Useful to use before trying to access a Chess piece data member.
    public boolean hasNoPieceAt(int row, int column) {
        return (squares[row - 1][column - 1] == null);
    }

    // Function to check if a square has a piece.
    public boolean hasPieceAt(int row, int column) {
        return (squares[row - 1][column - 1] != null);
    }



    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // First, clear the entire board by setting all squares to null.
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 0; c++) {
                squares[r][c] = null;
            }
        }

        // Add White chess pieces at bottom row of board
        squares[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        squares[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        squares[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        // Add Black chess pieces to top row of board
        squares[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        squares[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        squares[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        squares[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        // Add all pawn pieces to board
        for (int col = 0; col < 8; col++) {
            squares[1][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[6][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }

    @Override
    public boolean equals(Object o) {
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
