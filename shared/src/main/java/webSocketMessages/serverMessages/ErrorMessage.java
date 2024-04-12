package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{

    private String errorMessage;
    public ErrorMessage(ServerMessageType type) {
        super(type);
    }

    public void setMessage(String msg) {
        this.errorMessage = msg;
    }

    public String getMessage() {
        return errorMessage;
    }
}
