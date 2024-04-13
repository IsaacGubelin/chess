package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {

    private String notification;
    public Notification(ServerMessageType type, String message) {
        super(type);
        this.notification = message;
    }

    public String getMessage() {
        return notification;
    }
}
