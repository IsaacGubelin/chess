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
        int r = position.getRow();
        int c = position.getColumn();

        // Before adding piece to square, verify that the square is vacant.
        if (squares[r][c] != null) {
            squares[r][c] = piece;
        }
        else {
            System.out.println("Space already occupied."); //TODO: Change to throw statement, or remove
        }
    }

    // Overload function that takes row and column info as parameters
    public void addPiece(int row, int col, ChessPiece piece) {
        // Before adding piece to square, verify that the square is vacant.
        if (squares[row][col] != null) {
            squares[row][col] = piece;
        }
        else {
            System.out.println("Space already occupied."); //TODO: Change to throw statement, or remove
        }
    }

    // Helper function for removing a piece from the board by setting it to null.
    // Called when a piece is captured.
    public void removePiece(ChessPosition position) {
        squares[position.getRow()][position.getColumn()] = null;
    }



    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
    }

    // Overloaded function if user gives row and column parameters instead.
    public ChessPiece getPiece(int row, int column) {
        return squares[row][column];
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
        for (int c = 0; c < 8; c++) {
            addPiece(1, c, whitePawn);
        }
        // Add remaining white pieces to board
        addPiece(0, 0, whiteRook);
        addPiece(0, 1, whiteKnight);
        addPiece(0, 2, whiteBishop);
        addPiece(0, 3, whiteQueen);
        addPiece(0, 4, whiteKing);
        addPiece(0, 5, whiteBishop);
        addPiece(0, 6, whiteKnight);
        addPiece(0, 7, whiteRook);

        // Add all BLACK PAWNS to board
        for (int c = 0; c < 8; c++) {
            addPiece(6, c, blackPawn);
        }
        // Remaining black pieces to board
        addPiece(7, 0, blackRook);
        addPiece(7, 1, blackKnight);
        addPiece(7, 2, blackBishop);
        addPiece(7, 3, blackQueen);
        addPiece(7, 4, blackKing);
        addPiece(7, 5, blackBishop);
        addPiece(7, 6, blackKnight);
        addPiece(7, 7, blackRook);
    }

    @Override
    public boolean equals(Object o) {
        if (this == null && o == null) return true;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.equals(squares, that.squares);
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
