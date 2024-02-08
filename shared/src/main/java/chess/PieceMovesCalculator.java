package chess;

import javax.imageio.stream.ImageOutputStream;
import java.util.HashSet;


public class PieceMovesCalculator {


    // Returns true if a position is within the bounds of the 8x8 Chessboard.
    private static boolean isInBounds(int row, int col) {
        return (row >= 1 && row < 9 && col >= 1 && col < 9);
    }

    // Returns true if a spot is in bounds and is either empty or has a piece of the opposing color.
    // CANNOT be used on pawns since they have a unique set of moves.
    private static boolean isClaimable(int row, int col, ChessBoard board, ChessGame.TeamColor color) {
        boolean isEmptySpot = board.hasNoPieceAt(row, col);
        return (isInBounds(row, col) && (isEmptySpot || board.getPiece(row, col).getTeamColor() != color));
    }



    // This function takes a board and position and determines all possible moves for the chess piece at
    // the given position. Each chess piece type has a helper function that specifically looks for its moves.
    public static HashSet<ChessMove> getAvailablePieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<>();

        // Only calculate moves for this position if it contains a chess piece.
        if (board.hasPieceAt(position.getRow(), position.getColumn())) {

            ChessPiece.PieceType type = board.getPiece(position).getPieceType(); // Determine the type of piece

            switch (type) {
                // The pawn is the only piece where the piece moves depend on team color.
                case PAWN:
                    if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK)
                        moves = getPawnMovesBlack(board, position); // Calculate moves for black pawn
                    else
                        moves = getPawnMovesWhite(board, position); // Calculate moves for white pawn
                    break;

                case ROOK:
                    moves = getRookMoves(board, position);
                    break;

                case KNIGHT:
                    moves = getKnightMoves(board, position);
                    break;

                case BISHOP:
                    moves = getBishopMoves(board, position);
                    break;

                // The queen's move abilities are a union of the available moves for a bishop and rook.
                case QUEEN:
                    moves = getRookMoves(board, position);
                    moves.addAll(getBishopMoves(board, position));
                    break;

                case KING:
                    moves = getKingMoves(board, position);
                    break;
            }
        }


        return moves;
    }

    // Takes in a position and evaluates possible castling moves.
    // CRITERIA FOR CASTLING:
    // 1. King is NOT currently in check
    // 2. No pieces between king and rook
    // 3. Neither the king nor rook has been moved
    // 4. The king does not pass through nor finish on a space that is in check.
    public static HashSet<ChessMove> getCastlingMoves(ChessBoard board, ChessPosition kingPos) {
        HashSet<ChessMove> castlingMoves = new HashSet<>();

        // Get current row and column
        int r = kingPos.getRow();
        int c = kingPos.getColumn();

        // Before checking requirements, verify that the king has not been moved.
        if (board.hasNoPieceAt(kingPos) || board.getPiece(kingPos).hasBeenMoved()) {
            return castlingMoves;   // If space is empty or not a king, return empty moves set.
        }

        // Make sure king is not in check
        if (KingCheckEvaluator.positionIsInRisk(board, kingPos)) {
            return castlingMoves;   // If in check, return empty moves set.
        }


        // CHECK FOR KING'S SIDE CASTLING
        if (canCastleKingSide(board, kingPos)) {
            // This is a chess move in which the king moves two spaces to the right.
            castlingMoves.add(new ChessMove(kingPos, new ChessPosition(r, c + 2)));
        }

        // CHECK FOR QUEEN'S SIDE CASTLING
        if (canCastleQueenSide(board, kingPos)) {
            // This is a chess move in which the king moves two spaces to the left.
            castlingMoves.add(new ChessMove(kingPos, new ChessPosition(r, c - 2)));
        }

        return castlingMoves;
    }

    // TODO: Rework these castling functions so they are totally error-free
    // A helper method that checks available castling on king's side.
    public static boolean canCastleKingSide(ChessBoard board, ChessPosition kingPos) {

        // Determine which row we are evaluating a castling move in
        ChessGame.TeamColor pieceColor = board.getPiece(kingPos).getTeamColor();
        int r = pieceColor == ChessGame.TeamColor.WHITE ? 1 : 8;

        // Check if there is an unmoved rook to the far right of the king (in its same row)
        if (board.hasNoPieceAt(r, 8)                    // Verify if there is a piece to far right.
                || board.getPiece(r, 8).hasBeenMoved()  // Verify it hasn't moved. (this means it's a rook)
                || board.getPiece(kingPos).hasBeenMoved()) {       // Make sure king hasn't been moved.
            return false;
        }

        // Check if there are any pieces between the king and rook.
        if (board.hasPieceAt(r, 6) || board.hasPieceAt(r, 7)) {
            return false;
        }

        // Move king one space to right. If in check, move it back
        board.addPiece(r, 6, new ChessPiece(pieceColor, ChessPiece.PieceType.KING));
        board.removePiece(kingPos);
        if (KingCheckEvaluator.positionIsInRisk(board, new ChessPosition(r, 6))) {  // If in check
            board.addPiece(kingPos, new ChessPiece(pieceColor, ChessPiece.PieceType.KING)); // Place king back
            board.removePiece(new ChessPosition(r, 6));
            return false;       // The king would pass through check. Cannot do castling on king side.
        }

        // Move king one more space to right. If in check, place it back
        board.addPiece(r, 7, new ChessPiece(pieceColor, ChessPiece.PieceType.KING));
        board.removePiece(new ChessPosition(r, 6));
        if (KingCheckEvaluator.positionIsInRisk(board, new ChessPosition(r, 7))) {  // If in check
            board.addPiece(kingPos, new ChessPiece(pieceColor, ChessPiece.PieceType.KING)); // Place king back
            board.removePiece(new ChessPosition(r, 7));
            return false;
        }

        // King is now verified to pass through and end in safety. Return to original position
        board.addPiece(kingPos, new ChessPiece(pieceColor, ChessPiece.PieceType.KING));
        board.removePiece(new ChessPosition(r, 7));

        // All required criteria met for king-side castling. Return true
        return true;
    }

    // A helper method that returns available castling moves on queen's side.
    private static boolean canCastleQueenSide(ChessBoard board, ChessPosition kingPos) {
        // Get the row and column of the position and the team color
        int r = kingPos.getRow(); // Used to access rook on same row

        ChessGame.TeamColor pieceColor = board.getPiece(kingPos).getTeamColor();

        // Check if there is an unmoved rook to the far left of the king (in its same row)
        if (board.hasNoPieceAt(r, 1)                    // Verify if there is a piece to far right.
                || board.getPiece(r, 1).hasBeenMoved()  // Verify it hasn't moved. (this means it's a rook)
                || board.getPiece(kingPos).hasBeenMoved()) {       // Make sure king hasn't been moved.
            return false;
        }

        // Check if there are any pieces between the king and rook.
        if (board.hasPieceAt(r, 2) || board.hasPieceAt(r, 3) || board.hasPieceAt(r, 4)) {
            return false;
        }

        // Move king one space to left. If in check, move it back
        board.addPiece(r, 4, new ChessPiece(pieceColor, ChessPiece.PieceType.KING));
        board.removePiece(kingPos);
        if (KingCheckEvaluator.positionIsInRisk(board, new ChessPosition(r, 4))) {  // If in check
            board.addPiece(kingPos, new ChessPiece(pieceColor, ChessPiece.PieceType.KING)); // Place king back
            board.removePiece(new ChessPosition(r, 4));
            return false;       // The king would pass through check. Cannot do castling on king side.
        }

        // Move king one more space to left. If in check, place it back
        board.addPiece(r, 3, new ChessPiece(pieceColor, ChessPiece.PieceType.KING));
        board.removePiece(new ChessPosition(r, 4));
        if (KingCheckEvaluator.positionIsInRisk(board, new ChessPosition(r, 3))) {  // If in check
            board.addPiece(kingPos, new ChessPiece(pieceColor, ChessPiece.PieceType.KING)); // Place king back
            board.removePiece(new ChessPosition(r, 3));
            return false;
        }

        // King is now verified to pass through and end in safety. Return to original position
        board.addPiece(kingPos, new ChessPiece(pieceColor, ChessPiece.PieceType.KING));
        board.removePiece(new ChessPosition(r, 3));

        // All required criteria met for queen-side castling. Return true
        return true;

    }


    // PAWN MOVES (Black) These pawns start in row 7 and move downward.
    private static HashSet<ChessMove> getPawnMovesBlack(ChessBoard board, ChessPosition oldPos) {
        HashSet<ChessMove> moves = new HashSet<>();

        // Keep row and column information handy
        int r = oldPos.getRow();
        int c = oldPos.getColumn();

        // First, check if pawn hasn't moved. Check 1 and 2 spaces ahead. If clear, pawn can move two spaces.
        if (r == 7 && board.hasNoPieceAt(r - 1, c) && board.hasNoPieceAt(r - 1, c)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r - 2, c)));
        }

        // Check for ordinary forward move availability. A promotion may occur if pawn is in row 2.
        if (r > 1 && board.hasNoPieceAt(r - 1, c)) {
            // Check if pawn is about to reach last row. If so, add promotion piece moves.
            if (r == 2) {
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c), ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c), ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c), ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c), ChessPiece.PieceType.QUEEN));
            }
            else { // Otherwise, add just a standard forward move.
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c)));
            }
        }

        // Check for possible capture moves. These may also result in promotion.
        // Check down and left for enemy white piece. Check that pawn is not at far left of board.
        if (c > 1 && r > 1 && board.hasPieceAt(r - 1, c - 1) &&
                board.getPiece(r - 1, c - 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
            // Check for promotion possibility
            if (r == 2) {
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c - 1), ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c - 1), ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c - 1), ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c - 1), ChessPiece.PieceType.QUEEN));
            }
            else { // Otherwise, add ordinary forward move
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c - 1)));
            }
        }
        // Check down and right for enemy white piece. Check that pawn is not at far right of board.
        if (c < 8 && r > 1 && board.hasPieceAt(r - 1, c + 1) &&
                board.getPiece(r - 1, c + 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
            // Check for promotion possibility
            if (r == 2) {
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c + 1), ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c + 1), ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c + 1), ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c + 1), ChessPiece.PieceType.QUEEN));
            }
            else { // Otherwise, add ordinary forward move
                moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c + 1)));
            }
        }
        // All possible black pawn moves collected.
        return moves;
    }

    // PAWN MOVES (White) These pawns start in row 7 and move downward.
    private static HashSet<ChessMove> getPawnMovesWhite(ChessBoard board, ChessPosition oldPos) {
        HashSet<ChessMove> moves = new HashSet<>();

        // Keep row and column information handy
        int r = oldPos.getRow();
        int c = oldPos.getColumn();

        // First, check if pawn hasn't moved. Check 1 and 2 spaces ahead. If clear, pawn can move two spaces.
        if (r == 2 && board.hasNoPieceAt(r + 1, c) && board.hasNoPieceAt(r + 2, c)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r + 2, c)));
        }

        // Check for ordinary forward move availability. A promotion may occur if pawn is in row 7.
        if (r < 8 && board.hasNoPieceAt(r + 1, c)) {
            // Check if pawn is about to reach last row. If so, add promotion piece moves.
            if (r == 7) {
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c), ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c), ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c), ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c), ChessPiece.PieceType.QUEEN));
            }
            else { // Otherwise, add just a standard forward move.
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c)));
            }
        }

        // Check for possible capture moves. These may also result in promotion.
        // Check up and left for enemy black piece. Check that pawn is not at far left of board.
        if (c > 1 && r < 8 && board.hasPieceAt(r + 1, c - 1) &&
                board.getPiece(r + 1, c - 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
            // Check for promotion possibility
            if (r == 7) {
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c - 1), ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c - 1), ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c - 1), ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c - 1), ChessPiece.PieceType.QUEEN));
            }
            else { // Otherwise, add ordinary forward move
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c - 1)));
            }
        }
        // Check up and right for enemy black piece. Check that pawn is not at far right of board.
        if (c < 8 && r < 8 && board.hasPieceAt(r + 1, c + 1) &&
                board.getPiece(r + 1, c + 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
            // Check for promotion possibility
            if (r == 7) {
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c + 1), ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c + 1), ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c + 1), ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c + 1), ChessPiece.PieceType.QUEEN));
            }
            else { // Otherwise, add ordinary forward move
                moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c + 1)));
            }
        }
        // All possible white pawn moves collected.
        return moves;
    }

    // ROOK MOVES
    protected static HashSet<ChessMove> getRookMoves(ChessBoard board, ChessPosition oldPos) {
        HashSet<ChessMove> moves = new HashSet<>();

        // Get current row, column, and piece color info
        int r = oldPos.getRow();
        int c = oldPos.getColumn();
        ChessGame.TeamColor color = board.getPiece(oldPos).getTeamColor();

        // Start checking moves upwards for empty available spots
        for (int newRow = r + 1; newRow < 9; newRow++) {
            // Rook cannot keep moving if it finds a piece in the way.
            if (board.hasPieceAt(newRow, c)) {
                // If rook encounters a piece it can capture, store the move and break the loop.
                if (isClaimable(newRow, c, board, color)) {
                    moves.add(new ChessMove(oldPos, new ChessPosition(newRow, c)));
                }
                // No more moves in this direction to check for
                break;
            }

            moves.add(new ChessMove(oldPos, new ChessPosition(newRow, c)));
        }
        // Repeat for downwards positions
        for (int newRow = r - 1; newRow >= 1; newRow--) {
            // Rook cannot keep moving if it finds a piece in the way.
            if (board.hasPieceAt(newRow, c)) {
                // If rook encounters a piece it can capture, store the move and break the loop.
                if (isClaimable(newRow, c, board, color)) {
                    moves.add(new ChessMove(oldPos, new ChessPosition(newRow, c)));
                }
                // No more moves in this direction to check for
                break;
            }

            moves.add(new ChessMove(oldPos, new ChessPosition(newRow, c)));
        }

        // Check leftwards. Now column is the changing variable.
        for (int newCol = c - 1; newCol >= 1; newCol--) {
            // Rook cannot keep moving if it finds a piece in the way.
            if (board.hasPieceAt(r, newCol)) {
                // If rook encounters a piece it can capture, store the move and break the loop.
                if (isClaimable(r, newCol, board, color)) {
                    moves.add(new ChessMove(oldPos, new ChessPosition(r, newCol)));
                }
                // No more moves in this direction to check for
                break;
            }

            moves.add(new ChessMove(oldPos, new ChessPosition(r, newCol)));
        }
        // Check Right.
        for (int newCol = c + 1; newCol < 9; newCol++) {
            // Rook cannot keep moving if it finds a piece in the way.
            if (board.hasPieceAt(r, newCol)) {
                // If rook encounters a piece it can capture, store the move and break the loop.
                if (isClaimable(r, newCol, board, color)) {
                    moves.add(new ChessMove(oldPos, new ChessPosition(r, newCol)));
                }
                // No more moves in this direction to check for
                break;
            }

            moves.add(new ChessMove(oldPos, new ChessPosition(r, newCol)));
        }

        return moves;
    }

    // KNIGHT MOVES
    // Knights are the only pieces that can jump over other pieces.
    // The knight can move in L-patterns to a maximum of 8 given positions.
    private static HashSet<ChessMove> getKnightMoves(ChessBoard board, ChessPosition oldPos) {
        HashSet<ChessMove> moves = new HashSet<>();

        // Get position and color info
        int r = oldPos.getRow();
        int c = oldPos.getColumn();
        ChessGame.TeamColor color = board.getPiece(oldPos).getTeamColor();

        // Start checking positions that can be reached with an L-shaped path.
        if (isInBounds(r + 2, c - 1) && isClaimable(r + 2, c - 1, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r + 2, c - 1)));
        }
        if (isInBounds(r + 2, c + 1) && isClaimable(r + 2, c + 1, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r + 2, c + 1)));
        }
        if (isInBounds(r + 1, c + 2) && isClaimable(r + 1, c + 2, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c + 2)));
        }
        if (isInBounds(r - 1, c + 2) && isClaimable(r - 1, c + 2, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c + 2)));
        }
        if (isInBounds(r - 2, c + 1) && isClaimable(r - 2, c + 1, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r - 2, c + 1)));
        }
        if (isInBounds(r - 2, c - 1) && isClaimable(r - 2, c - 1, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r - 2, c - 1)));
        }
        if (isInBounds(r - 1, c - 2) && isClaimable(r - 1, c - 2, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c - 2)));
        }
        if (isInBounds(r + 1, c - 2) && isClaimable(r + 1, c - 2, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c - 2)));
        }
        // All possible knight moves checked.
        return moves;
    }

    // BISHOP MOVES
    // Very similar to rook move calculating logic, but diagonal.
    protected static HashSet<ChessMove> getBishopMoves(ChessBoard board, ChessPosition oldPos) {
        HashSet<ChessMove> moves = new HashSet<>();

        // Get current row, column, and piece color info
        int r = oldPos.getRow();
        int c = oldPos.getColumn();
        ChessGame.TeamColor color = board.getPiece(oldPos).getTeamColor();

        // Uses for-loops with two iterators that increment in parallel. (One for row, one for column)
        // Start checking up and to the right
        for (int newRow = r + 1, newCol = c + 1; (newRow < 9 && newCol < 9); newRow++, newCol++) {
            // Rook cannot keep moving if it finds a piece in the way.
            if (board.hasPieceAt(newRow, newCol)) {
                // If rook encounters a piece it can capture, store the move and break the loop.
                if (isClaimable(newRow, newCol, board, color)) {
                    moves.add(new ChessMove(oldPos, new ChessPosition(newRow, newCol)));
                }
                // No more moves in this direction to check for
                break;
            }
            // Space must be empty. Add to collection and keep checking for more available moves.
            moves.add(new ChessMove(oldPos, new ChessPosition(newRow, newCol)));
        }

        // Check down and to the right
        for (int newRow = r - 1, newCol = c + 1; (newRow >= 1 && newCol < 9); newRow--, newCol++) {
            // Rook cannot keep moving if it finds a piece in the way.
            if (board.hasPieceAt(newRow, newCol)) {
                // If rook encounters a piece it can capture, store the move and break the loop.
                if (isClaimable(newRow, newCol, board, color)) {
                    moves.add(new ChessMove(oldPos, new ChessPosition(newRow, newCol)));
                }
                // No more moves in this direction to check for
                break;
            }
            // Space must be empty. Add to collection and keep checking for more available moves.
            moves.add(new ChessMove(oldPos, new ChessPosition(newRow, newCol)));
        }

        // Start checking down and to the left
        for (int newRow = r - 1, newCol = c - 1; (newRow >= 1 && newCol >= 1); newRow--, newCol--) {
            // Rook cannot keep moving if it finds a piece in the way.
            if (board.hasPieceAt(newRow, newCol)) {
                // If rook encounters a piece it can capture, store the move and break the loop.
                if (isClaimable(newRow, newCol, board, color)) {
                    moves.add(new ChessMove(oldPos, new ChessPosition(newRow, newCol)));
                }
                // No more moves in this direction to check for
                break;
            }
            // Space must be empty. Add to collection and keep checking for more available moves.
            moves.add(new ChessMove(oldPos, new ChessPosition(newRow, newCol)));
        }

        // Start checking up and to the left
        for (int newRow = r + 1, newCol = c - 1; (newRow < 9 && newCol >= 1); newRow++, newCol--) {
            // Rook cannot keep moving if it finds a piece in the way.
            if (board.hasPieceAt(newRow, newCol)) {
                // If rook encounters a piece it can capture, store the move and break the loop.
                if (isClaimable(newRow, newCol, board, color)) {
                    moves.add(new ChessMove(oldPos, new ChessPosition(newRow, newCol)));
                }
                // No more moves in this direction to check for
                break;
            }
            // Space must be empty. Add to collection and keep checking for more available moves.
            moves.add(new ChessMove(oldPos, new ChessPosition(newRow, newCol)));
        }
        return moves;
    }

    // KING MOVES
    // The king piece can only move one single space towards any of the 8 squares around it.
    private static HashSet<ChessMove> getKingMoves(ChessBoard board, ChessPosition oldPos) {
        HashSet<ChessMove> moves = new HashSet<>();

        // Get piece info
        int r = oldPos.getRow();
        int c = oldPos.getColumn();
        ChessGame.TeamColor color = board.getPiece(oldPos).getTeamColor();

        // Start with checking space above, and then check the other squares in a clockwise manner
        if (isInBounds(r + 1, c) && isClaimable(r + 1, c, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c)));
        } // Now check up and right
        if (isInBounds(r + 1, c + 1) && isClaimable(r + 1, c + 1, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c + 1)));
        } // Check right
        if (isInBounds(r, c + 1) && isClaimable(r, c + 1, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r, c + 1)));
        } // Check down and right
        if (isInBounds(r - 1, c + 1) && isClaimable(r - 1, c + 1, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c + 1)));
        }
        if (isInBounds(r - 1, c) && isClaimable(r - 1, c, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c)));
        }
        if (isInBounds(r - 1, c - 1) && isClaimable(r - 1, c - 1, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r - 1, c - 1)));
        }
        if (isInBounds(r, c - 1) && isClaimable(r, c - 1, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r, c - 1)));
        }
        if (isInBounds(r + 1, c - 1) && isClaimable(r + 1, c - 1, board, color)) {
            moves.add(new ChessMove(oldPos, new ChessPosition(r + 1, c - 1)));
        }
        // All possible king moves checked.
        return moves;
    }

}
