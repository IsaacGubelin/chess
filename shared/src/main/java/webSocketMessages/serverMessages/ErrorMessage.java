package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{

    private String errorMessage;
    public ErrorMessage(ServerMessageType type) {
        super(type);
    }

    public String getMessage() {
        return errorMessage;
    }
}
