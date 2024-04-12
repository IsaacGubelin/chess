package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {

    private int gameID;
    private ChessGame.TeamColor teamColor;
    public JoinPlayerCommand(String authToken, int gameID, String color) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.teamColor = color.equals("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
    }

    public int getGameID() {                            // Getter for game ID
        return gameID;
    }

    public ChessGame.TeamColor getRequestedColor() {    // Getter for team color
        return teamColor;
    }

}
