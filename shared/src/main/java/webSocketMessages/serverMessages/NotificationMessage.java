package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {

    private String notification;
    public NotificationMessage(ServerMessageType type) {
        super(type);
    }

    public String getMessage() {
        return notification;
    }
}
