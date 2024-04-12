package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {

    private int gameID;
    private ChessMove move;
    public MakeMove(String authToken) {
        super(authToken);
    }
}
