import chess.*;
import org.eclipse.jetty.server.Server;


public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        // Generic server object
        Server server = new Server();

        // FIXME: Need to run server
    }
}