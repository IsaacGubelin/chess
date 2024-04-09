package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{

    private ChessGame game;
    public LoadGameMessage(ServerMessageType type) {
        super(type);
    }

    public ChessGame getGame() {
        return game;
    }
}
