package facade;
import com.google.gson.Gson;

import model.*;
import resException.ResponseException;
import webSocket.ServiceMessageHandler;
import webSocket.ClientWS;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;

import java.io.*;
import java.net.*;


public class WSFacade {
    public final String serverUrl;                                  // Connect to server
    private final ClientWS clientSocket;                     // Access the methods in the web socket facade
    public final String REQ_HEADER_AUTHORIZATION = "authorization"; // Used as key for HTTP request headers

    public WSFacade(String url, ServiceMessageHandler msgHandler) throws ResponseException {
        clientSocket = new ClientWS(url, msgHandler);
        serverUrl = url;
    }

    public WSFacade(int port, ServiceMessageHandler msgHandler) throws ResponseException {
        String url = "http://localhost:";   // Create url
        url += port;                        // Add port number
        clientSocket = new ClientWS(url, msgHandler);
        serverUrl = url;
    }



    /**
     * A function called by the chess client to join an available game.
     * @param gameReqData Contains needed data about which game and team color to join
     * @param authToken Used for user verification
     */
    public void joinGame(String authToken, GameRequestData gameReqData) throws ResponseException {
        try {
            JoinPlayer cmd = new JoinPlayer(authToken, gameReqData.gameID(), gameReqData.playerColor());
            clientSocket.session.getBasicRemote().sendText(new Gson().toJson(cmd));

        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leaveGame(String authToken, int gameID) throws ResponseException {
        Leave leaveCmd = new Leave(authToken, gameID);
        try {
            clientSocket.session.getBasicRemote().sendText(new Gson().toJson(leaveCmd));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

}
