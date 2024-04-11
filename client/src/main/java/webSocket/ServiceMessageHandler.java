package webSocket;


import webSocketMessages.serverMessages.ServerMessage;


public interface ServiceMessageHandler {
    void notify(String message);
}
