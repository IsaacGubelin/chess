package serviceTests;

import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.GameService;
import service.LoginOutService;
import service.RegisterService;

import java.sql.SQLDataException;
import java.util.Collection;
import java.util.HashSet;

public class ServiceTests {

    @Test
    @DisplayName("Check if clear endpoint empties all databases")
    public void testClearApplication() {

//        MemoryAuthDAO aDAO = new MemoryAuthDAO();   // New database objects for auth, user, and game data
//        MemoryUserDAO uDAO = new MemoryUserDAO();
//        MemoryGameDAO gDAO = new MemoryGameDAO();
//
//        aDAO.createAuth("User1");           // Add some auth data
//        aDAO.createAuth("User2");
//
//        try {                                       // Add new user data
//            uDAO.createUser(new UserData("userName", "123456789", "email"));
//            uDAO.createUser(new UserData("anotherName", "swordfish", "best@freemail.com"));
//        } catch (AlreadyTakenException dataEx) {
//        }
//
//        gDAO.createGame("TheGame");     // Add games to game database
//        gDAO.createGame("TheOtherGame");
//        gDAO.createGame("A third game");
//
//        // All databases should now contain data
//        Assertions.assertFalse(gDAO.getGameDatabase().isEmpty() ||
//                aDAO.authDataTable.isEmpty() ||
//                uDAO.userTable.isEmpty());
//
//        // TESTING CLEAR ENDPOINT
//        ClearService.clearService(uDAO, gDAO, aDAO);
//        Assertions.assertTrue(gDAO.getGameDatabase().isEmpty() &&
//                                aDAO.authDataTable.isEmpty() &&
//                                uDAO.userTable.isEmpty());
    }

    @Test
    @DisplayName("Check if register service accepts a properly registered user")
    public void testRegisterAccept() {

//        MemoryAuthDAO aDAO = new MemoryAuthDAO();   // Create databases required by registration endpoint
//        MemoryUserDAO uDAO = new MemoryUserDAO();

        String name = "Joe";
        String password = "Swordfish";
        String email = "another@yahmail.com";
        String token = "";

//        try {
//            token = RegisterService.register(new UserData(name, password, email), uDAO, aDAO);
//        } catch (BadRequestException badEx) {
//            Assertions.fail("Registration failed, bad request exception thrown.");
//        } catch (AlreadyTakenException alrEx) {
//            Assertions.fail("Registration failed, data access exception thrown.");
//        }
//
//        // Check that all entries are in user database
//        Assertions.assertEquals(uDAO.getUser(name).username(), name);
//        Assertions.assertEquals(uDAO.getUser(name).password(), password);
//        Assertions.assertEquals(uDAO.getUser(name).email(), email);
//
//        // Check that auth database has new user entry
//        Assertions.assertEquals(aDAO.getAuth(token).username(), name);
    }

    @Test
    @DisplayName("Check if register service rejects an incorrectly registered user")
    public void testRegisterRejectNullValues() {
//        MemoryAuthDAO aDAO = new MemoryAuthDAO();   // Create databases required by registration endpoint
//        MemoryUserDAO uDAO = new MemoryUserDAO();

        String name = null;     // Simulate missing name
        String password = "Swordfish";
        String email = "another@yahmail.com";

        // Test thrown exception for null username
//        Assertions.assertThrows(BadRequestException.class, () ->
//                RegisterService.register(new UserData(name, password, email), uDAO, aDAO));

        String newName = "Bob";   // Give name variable a non-null value
        String newMail = null;   // Make email variable null

        // Test thrown exception for null email
//        Assertions.assertThrows(BadRequestException.class, () ->
//                RegisterService.register(new UserData(newName, password, newMail), uDAO, aDAO));

    }

    @Test
    @DisplayName("Check if register service rejects an incorrectly registered user")
    public void testRegisterRejectTakenName() {
//        MemoryAuthDAO aDAO = new MemoryAuthDAO();   // Create databases required by registration endpoint
//        MemoryUserDAO uDAO = new MemoryUserDAO();

        String name = "Bob";     // Simulate missing name
        String password = "Swordfish";
        String email = "another@yahmail.com";

//        try {
//            RegisterService.register(new UserData(name, password, email), uDAO, aDAO);
//        } catch (AlreadyTakenException alrEx) {
//        } catch (BadRequestException badEx) {
//        }
//
//        // Test thrown exception for null username
//        Assertions.assertThrows(AlreadyTakenException.class, () ->
//                RegisterService.register(new UserData(name, password, email), uDAO, aDAO));
    }

    @Test
    @DisplayName("Check for successful login")
    public void loginSuccess() {
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // Create new databases
//        MemoryUserDAO userDAO = new MemoryUserDAO();

        String name = "Joe";    // Username and other needed data for this test
        String password = "beans123";
        String email = "mail";
        String token = "";  // Used to check that user exists before commencing test

        // Add new user to database and make auth data
//        try {
//            userDAO.createUser(new UserData(name, password, email));
//            token = authDAO.createAuth(name);
//        } catch (AlreadyTakenException dataEx) {
//            Assertions.fail("AlreayTakenException thrown.");
//        }
//
//        Assertions.assertEquals(authDAO.getAuth(token).username(), name); // Verify that auth data was added
//
//        authDAO.clearAuthDatabase();    // Simulate a logout by clearing authTokens
//        String newToken = "";            // Use to retrieve new authToken from login

        // Attempt to log same user back in
//        try {
//            newToken = LoginOutService.login(new UserData(name, password, email), userDAO, authDAO).authToken();
//        } catch (BadRequestException badEx) {
//            Assertions.fail("Login failed, bad request exception thrown.");
//        } catch (UnauthorizedException unEx) {
//            Assertions.fail("Login failed, unauthorized exception thrown.");
//        }
//
//        Assertions.assertEquals(authDAO.getAuth(newToken).username(), name); // Check if username is in auth database
    }

    @Test
    @DisplayName("Check for rejected login from wrong password")
    public void loginUnauthorized() {
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // Create new databases
//        MemoryUserDAO userDAO = new MemoryUserDAO();
//
        String name = "Joe";    // Username and other needed data for this test
        String password = "beans123";
        String wrongWord = "321beans";
        String email = "mail";
//
//        // Add new user to database and make auth data
//        try {
//            userDAO.createUser(new UserData(name, password, email));
//            authDAO.createAuth(name);
//        } catch (AlreadyTakenException dataEx) {
//            Assertions.fail("AlreayTakenException thrown.");
//        }

//        authDAO.clearAuthDatabase();    // Simulate a logout by clearing authTokens
//
//        // Attempt to log same user back in
//        try {
//            LoginOutService.login(new UserData(name, password, email), userDAO, authDAO);
//        } catch (BadRequestException badEx) {
//            Assertions.fail("Login failed, bad request exception thrown.");
//        } catch (UnauthorizedException unEx) {
//            Assertions.fail("Login failed, unauthorized exception thrown.");
//        }
//
//        // Should throw unauthorized exception when given wrong password
//        Assertions.assertThrows(UnauthorizedException.class, () ->
//                LoginOutService.login(new UserData(name, wrongWord, email), userDAO, authDAO));
    }
    @Test
    @DisplayName("Check for successful logout")
    public void logoutSuccess() {
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // Make an auth database
        String name = "Benjamin";
//        String token = authDAO.createAuth(name);        // Add new auth data and retrieve token

//        Assertions.assertEquals(authDAO.getAuth(token).username(), name);   // Verify if auth data was made
//
//        // Attempt logout
//        try {
//            LoginOutService.logout(token, authDAO);
//        } catch (UnauthorizedException unEx) {
//            Assertions.fail("Logout test failed.");
//        }
//        Assertions.assertTrue(authDAO.authDataTable.isEmpty());    // Check that auth data is gone
    }

    @Test
    @DisplayName("Test failed logout")
    public void logoutWithWrongAuthToken() {
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // Make an auth database
//        String name = "Benjamin";
//        String wrongToken = "3aWRONG-TOKEN9tr4k";       // Make a token with wrong format and value
//        String correctToken = authDAO.createAuth(name); // Add new auth data and retrieve correct token
//
//        Assertions.assertEquals(authDAO.getAuth(correctToken).username(), name);   // Verify if auth data was made
//
//        // Attempt logout with incorrect authToken
//        Assertions.assertThrows(UnauthorizedException.class, () -> LoginOutService.logout(wrongToken, authDAO));
    }

    @Test
    @DisplayName("Test successful request for listGames service")
    public void listGamesSuccess() {
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // Make an auth database
//        MemoryGameDAO gameDAO = new MemoryGameDAO();    // Make games database
//        gameDAO.createGame("ChessGame1");       // Create chess game
        String name = "Benjamin";                       // Start making new auth data
//        String correctToken = authDAO.createAuth(name); // Add new auth data and retrieve correct token
//        Assertions.assertEquals(authDAO.getAuth(correctToken).username(), name);   // Verify if auth data was made
//
//        Collection<GameData> listOfGames = GameService.getGames(gameDAO);   // Retrieve games
//        Assertions.assertTrue(!listOfGames.isEmpty());  // Verify that the container is not empty
    }

    @Test
    @DisplayName("Test listGames rejection from wrong authToken")
    public void listGamesWrongAuthToken() {
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // Make an auth database
//        MemoryGameDAO gameDAO = new MemoryGameDAO();    // Make games database
//        gameDAO.createGame("Game1");            // Create game and add to database
//        String token = authDAO.createAuth("Bob");   // Add auth data and get token
        String wrongToken = "notRealToken";             // Make a fake token

        HashSet<GameData> games = new HashSet<>();

        // Will NOT retrieve games if auth database doesn't have authToken.
//        if (authDAO.hasAuth(wrongToken)) {
//            games = (HashSet<GameData>) GameService.getGames(gameDAO);
//            Assertions.fail("Error: retrieved games with incorrect authToken");
//        }
        Assertions.assertTrue(games.isEmpty()); // Should be empty
    }

    @Test
    @DisplayName("Test successfully created game")
    public void createGameServiceSuccess() {
//        MemoryGameDAO gameDAO = new MemoryGameDAO();    // Create games database
//        int gameID = 0;
//        try {
//            gameID = GameService.createGame("Game1", gameDAO); // Create a game
//        } catch (DataAccessException dataEx) {
//            Assertions.fail("Error: failed to create game");
//        } catch (BadRequestException badEx) {
//            Assertions.fail("Error: bad request");
//        }
//
//        // Verify that game was created and returned a nonzero id
//        Assertions.assertTrue(!gameDAO.getGameDatabase().isEmpty());
//        Assertions.assertTrue(gameID != 0);
    }

    @Test
    @DisplayName("Test for when name parameter is missing data")
    public void createGameFailureTest() {
//        MemoryGameDAO gameDAO = new MemoryGameDAO();    // Create games database
        String gameName = null;
//        Assertions.assertThrows(BadRequestException.class, () -> GameService.createGame(gameName, gameDAO));
    }


    @Test
    @DisplayName("Test successful joining into chess game for white team")
    public void joinWhiteTeamSuccess() {
        MemoryGameDAO gameDAO = null;    // New games database
        MemoryAuthDAO authDAO = null;    // New auth database
        try {
            gameDAO = new MemoryGameDAO();
            authDAO = new MemoryAuthDAO();
        } catch (SQLDataException e) {
            throw new RuntimeException(e);
        }

        String gameName = "Game1";                      // Name for new chess game
        String userName = "Freddy";                     // Username for client

        String token = authDAO.createAuth(userName);        // Make auth data for user and get token
        int gameID = gameDAO.createGame("Game1"); // Create new game and retrieve game ID

        gameDAO.updateWhiteUsername(gameID, userName);      // Add user to white team of chess game
        String whiteTeamName = gameDAO.getGameDatabase().get(gameID).whiteUsername(); // Retrieve newly added data
        Assertions.assertTrue(whiteTeamName.equals(userName));  // Check if username was succesfully added
    }

    @Test
    @DisplayName("Test alreadyTaken exception for joinGame request")
    public void joinWhiteTeamReject() {
//        MemoryGameDAO gameDAO = new MemoryGameDAO();    // New games database
//        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // New auth database
        String gameName = "Game1";                      // Name for new chess game
        String userName = "Freddy";                     // Username for client
        String otherUser = "Bonnie";                    // User trying to join already-taken team
        String requestedTeam = "WHITE";                 // Team that second user will try to join

//        authDAO.createAuth(userName);                   // Add both users to auth table
//        String token = authDAO.createAuth(otherUser);   // Keep second user's token for test
//        int gameID = gameDAO.createGame(gameName);      // Create new game and retrieve game ID
//
//        gameDAO.updateWhiteUsername(gameID, userName);  // Add first user to white team of chess game
//
//        // Adding second user to the same game's white game should throw an exception.
//        Assertions.assertThrows(AlreadyTakenException.class, () ->
//                GameService.joinGame(gameID, requestedTeam, token, gameDAO, authDAO));
    }



}
