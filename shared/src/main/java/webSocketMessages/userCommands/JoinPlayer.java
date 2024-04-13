package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {

    private int gameID;
    private ChessGame.TeamColor teamColor;
    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor color) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.teamColor = color;
    }

    public int getGameID() {                            // Getter for game ID
        return gameID;
    }

    public ChessGame.TeamColor getRequestedColor() {    // Getter for team color
        return teamColor;
    }

}
