package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    private int gameID;
    private ChessMove move;
    public MakeMoveCommand(String authToken) {
        super(authToken);
    }
}
