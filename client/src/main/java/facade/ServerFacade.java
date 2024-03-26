package facade;
import com.google.gson.Gson;

import model.*;
import resException.ResponseException;
import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class ServerFacade {
    private final String serverUrl;                                     // Connect to server
    private final String REQ_HEADER_AUTHORIZATION = "authorization";    // Used as key for HTTP request headers

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public AuthData register(UserData userData) throws ResponseException {
        String path = "/user";      // HTTP path
        return this.makeRequest("POST", path, userData, AuthData.class);
    }


    public AuthData login(UserData userData) throws ResponseException {
        String path = "/session";
        return this.makeRequest("POST", path, userData, AuthData.class);
    }

    public void logout(String authToken) throws ResponseException {
        String path = "/session";
        String method = "DELETE";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);              // Set the request method (GET, DELETE, POST, etc)
            http.setDoOutput(true);                     // Indicate that the connection will output data

            http.addRequestProperty(REQ_HEADER_AUTHORIZATION, authToken);
            http.connect();                             // Connect to server
            throwIfNotSuccessful(http);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public GameIDData createGame(String authToken, String gameName) throws ResponseException {
        String path = "/game";
        String method = "POST";
        GameRequestData newGameData = new GameRequestData(gameName, null, 0);

        try {
            URL url = (new URI(serverUrl + path).toURL());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);              // Set the request method (GET, DELETE, POST, etc)
            http.setDoOutput(true);                     // Indicate that the connection will output data

            http.addRequestProperty(REQ_HEADER_AUTHORIZATION, authToken);   // Add auth token to http header
            writeBody(newGameData, http);

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, GameIDData.class);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public ListGamesData getGamesList(String authToken) throws ResponseException {
        String path = "/game";
        String method = "GET";
        try {
            URL url = (new URI(serverUrl + path).toURL());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);              // Set the request method (GET, DELETE, POST, etc)
            http.setDoOutput(true);                     // Indicate that the connection will output data

            http.addRequestProperty(REQ_HEADER_AUTHORIZATION, authToken);   // Add auth token to http header
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, ListGamesData.class);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);              // Set the request method (GET, DELETE, POST, etc)
            http.setDoOutput(true);                     // Indicate that the connection will output data

            writeBody(request, http);
            http.connect();                             // Connect to server
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    // Reads the response body from the input stream of the HTTP connection.
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {  // If content length is negative, there is content to read
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);  // Deserialize
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    // Returns true if status code is 200-299 (good)
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}
