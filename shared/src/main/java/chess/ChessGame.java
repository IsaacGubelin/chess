package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {
        ChessGameInit(); // Initialize the game
    }

    // Private variables for determining game status
    private TeamColor turn;
    private ChessBoard board;

    // These positions are checked each turn to check if either king is in check
    private ChessPosition blackKingLocation;
    private ChessPosition whiteKingLocation;



    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        // Get the available moves for this piece from the pieceMovesCalculator class
        // "moves" has all standard moves for the piece. "validMoves" takes the moves that keep the king safe.
        HashSet<ChessMove> moves = PieceMovesCalculator.getAvailablePieceMoves(board, startPosition);
        HashSet<ChessMove> validMoves = new HashSet<>(); // This will store the valid moves

        // If the position is empty, return empty collection
        if (board.hasNoPieceAt(startPosition.getRow(), startPosition.getColumn())) {
            return moves;
        }

        // Get piece type, color, and if it has been moved
        TeamColor color = board.getPiece(startPosition).getTeamColor();
        ChessPiece.PieceType type = board.getPiece(startPosition).getPieceType();
        boolean moved = board.getPiece(startPosition).hasBeenMoved();

        // Create a temporary chess piece for pieces occupying prospective end positions
        ChessPiece tempPiece = null;

        // Iterate through each move and verify which ones keep the corresponding king piece safe
        // Temporarily place the piece in the end position of each move to check king's safety
        for (ChessMove move : moves) {

            // Add identical piece to end position and remove from start position
            tempPiece = board.getPiece(move.getEndPosition()); // Keep track of temporarily captured pieces.
            board.addPiece(move.getEndPosition(), new ChessPiece(color, type));
            board.removePiece(startPosition);

            // Check the status of the king. If the team's king in check, the move is invalid and removed.
            if (isInCheck(color)) {
                board.addPiece(startPosition, new ChessPiece(color, type)); // Move piece back to old position
                board.removePiece(move.getEndPosition());
                if (moved) {                                                // Restore move boolean
                    board.getPiece(startPosition).setMoveFlagHigh();
                }
                if (tempPiece != null) {
                    // Put back piece captured (if applicable)
                    ChessPiece oldPiece = new ChessPiece(tempPiece.getTeamColor(), tempPiece.getPieceType());
                    board.addPiece(move.getEndPosition(), oldPiece);
                }
                continue;                                           // Skip adding this move and check next move
            }

            validMoves.add(move);                                   // Otherwise, add to valid moves collection.
            board.addPiece(startPosition, new ChessPiece(color, type)); // Then put back in original position.
            board.removePiece(move.getEndPosition());
            if (moved) {                                                // Restore move boolean
                board.getPiece(startPosition).setMoveFlagHigh();
            }

            if (tempPiece != null) {                            // Put back piece captured (if applicable)
                board.addPiece(move.getEndPosition(),
                        new ChessPiece(tempPiece.getTeamColor(), tempPiece.getPieceType()));
            }
        }

        // Lastly, add possible castling moves if applicable.
        // MakeMove will take care of carrying out this move.
        validMoves.addAll(PieceMovesCalculator.getCastlingMoves(board, startPosition));


        // All valid moves are now collected. Return collection of valid chess moves.
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        // Get type and color for piece being moved
        ChessPiece.PieceType type = board.getPiece(move.getStartPosition()).getPieceType();
        TeamColor pieceColor = board.getPiece(move.getStartPosition()).getTeamColor();

        // Determine the valid moves for the piece. This will determine if the move keeps the king safe.
        HashSet<ChessMove> availableMoves = new HashSet<>();
        availableMoves.addAll(validMoves(move.getStartPosition()));

        // If the given move is not available, or it isn't the team's turn, throw invalid move exception.
        if (!availableMoves.contains(move) || turn != pieceColor) {
            throw new InvalidMoveException();
        }

        // Check if this is a king-side castling move.
        // Evaluate if the piece is a king and if it is trying to castle to the right
        if (type == ChessPiece.PieceType.KING
            && move.getStartPosition().getColumn() == 5
            && move.getEndPosition().getColumn() == 7) {

            int kingRow = move.getStartPosition().getRow(); // Check which row we are in
            // Move rook to be next to king
            board.addPiece(new ChessPosition(kingRow, 6), new ChessPiece(pieceColor, ChessPiece.PieceType.ROOK));
            board.removePiece(new ChessPosition(kingRow, 8)); // Remove rook from old position
            board.getPiece(kingRow, 6).setMoveFlagHigh();  // Rook has now been moved
        }

        // Check if this is a queen-side castling move.
        // Evaluate if the piece is a king and if it is trying to castle to the right
        if (type == ChessPiece.PieceType.KING
                && move.getStartPosition().getColumn() == 5
                && move.getEndPosition().getColumn() == 3) {

            int kingRow = move.getStartPosition().getRow(); // Check which row we are in
            // Move rook to be next to king
            board.addPiece(new ChessPosition(kingRow, 4), new ChessPiece(pieceColor, ChessPiece.PieceType.ROOK));
            board.removePiece(new ChessPosition(kingRow, 1)); // Remove rook from old position
            board.getPiece(kingRow, 4).setMoveFlagHigh();  // Rook has now been moved
        }

        // Move the piece to its desired end position.
        board.addPiece(move.getEndPosition(), new ChessPiece(pieceColor, type)); // Place piece
        board.removePiece(move.getStartPosition());                              // Remove from old spot


        // Promote if the piece was a pawn.
        if (type == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) { // Check for valid promotion
            board.addPiece(move.getEndPosition(), new ChessPiece(pieceColor, move.getPromotionPiece()));
        }

        // Raise the hasMoved flag
        board.getPiece(move.getEndPosition()).setMoveFlagHigh();

        // Switch team turn.
        turn = (turn == TeamColor.BLACK) ? TeamColor.WHITE : TeamColor.BLACK;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        // Make a temporary position object that fetches the king piece location for the corresponding color
        //FIXME: use this in future
//        ChessPosition kingPosition = (teamColor == TeamColor.BLACK) ? blackKingLocation : whiteKingLocation;
        ChessPosition kingPosition = board.tempGetKingLoc(teamColor);

        return KingCheckEvaluator.positionIsInRisk(board, kingPosition);
    }

    // Validate a potential castling move
    // Takes in a ChessMove and confirms that it is a king piece that hasn't moved yet
    // Verifies that the move is trying to move the king two spaces to the left or right
    // Checks all necessary conditions required for castling
    //FIXME: make separate class for checking valid castling
//    private boolean isValidCastlingMove(ChessMove move) {
//        // Get starting row and column and piece color
//        int rStart = move.getStartPosition().getRow();
//        int cStart = move.getStartPosition().getColumn();
//
//        // Before proceeding with validation, check if the start position is empty or doesn't have a king.
//        if (board.hasNoPieceAt(rStart, cStart) ||
//            board.getPiece(rStart, cStart).getPieceType() != ChessPiece.PieceType.KING) {
//            return false;
//        }
//
//        // Get team color for piece being moved
//        TeamColor color = board.getPiece(rStart, cStart).getTeamColor();
//
//        // Check if the king hasn't moved and if its desired end position is two spaces away.
//        // FIXME: Add logic for non queen side castling
//
//    }

    // Validate a potential queen-side castling move
    // Checks all necessary conditions required for castling towards the queen's side of board
//    private boolean isValidCastlingMoveQueenSide(ChessMove move) {
//        // Get starting row and column and piece color
//        int rStart = move.getStartPosition().getRow();
//        int cStart = move.getStartPosition().getColumn();
//
//        // Before proceeding with validation, check if the start position is empty or doesn't have a king.
//        if (board.hasNoPieceAt(rStart, cStart) ||
//                board.getPiece(rStart, cStart).getPieceType() != ChessPiece.PieceType.KING) {
//            return false;
//        }
//
//        // Get team color for piece being moved
//        TeamColor color = board.getPiece(rStart, cStart).getTeamColor();
//
//        // Check if the king hasn't moved and if its desired end position is two spaces away.
//        // FIXME: Add logic
//
//    }



    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        // A checkmate happens when the king is in check and there are no available valid moves.
        return (isInCheck(teamColor) && hasNoValidMovesLeft(teamColor));
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        // A stalemate happens when the king is safe, but there are no available valid moves.
        return (!isInCheck(teamColor) && hasNoValidMovesLeft(teamColor));
    }

    // Helper method that tells if the given team has no valid moves.
    private boolean hasNoValidMovesLeft(TeamColor teamColor) {
        // Get all available moves for the team
        HashSet<ChessMove> availableMoves = new HashSet<>();

        // Go through every position on the chess board
        for (int row = 1; row < 9; row++) {                 // Iterate through each row
            for (int col = 1; col < 9; col++) {             // Iterate through each column
                // Record all available valid moves for every chess piece of the given team color
                if (board.hasPieceAt(row, col) && board.getPiece(row, col).getTeamColor() == teamColor) {
                    availableMoves.addAll(validMoves(new ChessPosition(row, col)));
                }
            }
        }
        return availableMoves.isEmpty();
    }



    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    // Initialize the chess board and set the team turn to the white team to start.
    private void ChessGameInit() {
        blackKingLocation = new ChessPosition(8, 5); // Construct and initialize king locations
        whiteKingLocation = new ChessPosition(1, 5);

        board = new ChessBoard();   // Construct the chess board private member
        board.resetBoard();         // Place all pieces in starting positions
        turn = TeamColor.WHITE;     // White team starts the game
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && board.equals(chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board);
    }
}
