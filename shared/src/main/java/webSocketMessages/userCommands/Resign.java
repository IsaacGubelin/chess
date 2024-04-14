package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    private int gameID;
    public Resign(String authToken) {

        super(authToken);
        this.commandType = CommandType.RESIGN;
    }
}
