package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    private int gameID; // TODO: MAKE SURE VARIABLE NAMES MATCH EXACTLY
    public Leave(String authToken) {
        super(authToken);
    }
}
