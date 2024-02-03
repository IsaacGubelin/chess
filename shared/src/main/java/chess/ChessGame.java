package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {

        ChessGameInit(); // Initialize the game

        //FIXME: Initialize anything else

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

        // Get the available moves from the pieceMovesCalculator class
        HashSet<ChessMove> moves = PieceMovesCalculator.getAvailablePieceMoves(board, startPosition);
        // TODO: If this is a king, verify which moves will put the king in check and discard those moves.
        // TODO: If this is any other piece, make sure to discard any moves that leave the king vulnerable


        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // TODO: Check and make sure that the move doesn't put their own king in check
            // If it does, throw invalid move exception
        // TODO: If the move is valid

        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        // Make a temporary position object that fetches the king piece location for the corresponding color
        ChessPosition kingPosition = teamColor == TeamColor.BLACK ? blackKingLocation : whiteKingLocation;

        return KingCheckEvaluator.positionIsInRisk(board, kingPosition);
    }



    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
        board.resetBoard();     // Place all pieces in starting positions
        turn = TeamColor.WHITE; // White team starts the game
    }
}
