package webSocket;


import resException.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;


public interface ServiceMessageHandler {
    void notify(String message) throws ResponseException;
}
