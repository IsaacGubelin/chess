package chess;

import java.util.ArrayList;
import java.util.Collection;

//import chess.KnightMovesCalculator;


public class PieceMovesCalculator extends KnightMovesCalculator {




    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();

        switch (type) {
            case PAWN:

            case ROOK:

            case KNIGHT:
                return knightMoves(board, myPosition);

            case BISHOP:

            case KING:

            case QUEEN:

        }



    }

}
