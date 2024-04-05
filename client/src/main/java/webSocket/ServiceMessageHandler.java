package webSocket;


import webSocketMessages.serverMessages.ServerMessage;

    public interface ServiceMessageHandler {
        void notify(ServerMessage message);
    }
}
