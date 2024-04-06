package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {

    private int gameID;
    private ChessGame.TeamColor teamColor;
    public JoinPlayerCommand(String authToken) {
        super(authToken);
    }
}
