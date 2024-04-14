package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {

    private int gameID;
    JoinObserver(String authToken) {

        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
    }
}
