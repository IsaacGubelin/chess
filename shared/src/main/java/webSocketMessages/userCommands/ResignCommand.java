package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    private int gameID;
    public ResignCommand(String authToken) {
        super(authToken);
    }
}
