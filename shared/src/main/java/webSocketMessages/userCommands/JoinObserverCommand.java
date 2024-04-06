package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {

    private int gameID;
    JoinObserverCommand(String authToken) {
        super(authToken);
    }
}
