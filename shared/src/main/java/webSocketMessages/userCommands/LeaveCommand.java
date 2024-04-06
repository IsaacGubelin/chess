package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand{
    private int gameID;
    public LeaveCommand(String authToken) {
        super(authToken);
    }
}
