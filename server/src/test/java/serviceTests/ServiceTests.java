package serviceTests;


import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.GameService;
import service.LoginOutService;
import service.RegisterService;

import java.util.Collection;

public class ServiceTests {

    @Test
    @DisplayName("Check if clear endpoint empties all databases")
    public void testClearApplication() {

        MemoryAuthDAO aDAO = new MemoryAuthDAO();   // New database objects for auth, user, and game data
        MemoryUserDAO uDAO = new MemoryUserDAO();
        MemoryGameDAO gDAO = new MemoryGameDAO();

        aDAO.createAuth("User1");           // Add some auth data
        aDAO.createAuth("User2");

        try {                                       // Add new user data
            uDAO.createUser(new UserData("userName", "123456789", "email"));
            uDAO.createUser(new UserData("anotherName", "swordfish", "best@freemail.com"));
        } catch (AlreadyTakenException dataEx) {
        }

        gDAO.createGame("TheGame");     // Add games to game database
        gDAO.createGame("TheOtherGame");
        gDAO.createGame("A third game");

        // All databases should now contain data
        Assertions.assertFalse(gDAO.getGameDatabase().isEmpty() ||
                aDAO.getAuthDataTable().isEmpty() ||
                uDAO.getUserTable().isEmpty());

        // TESTING CLEAR ENDPOINT
        ClearService.clearService(uDAO, gDAO, aDAO);
        Assertions.assertTrue(gDAO.getGameDatabase().isEmpty() &&
                                aDAO.getAuthDataTable().isEmpty() &&
                                uDAO.getUserTable().isEmpty());
    }

    @Test
    @DisplayName("Check if register service accepts a properly registered user")
    public void testRegisterAccept() {

        MemoryAuthDAO aDAO = new MemoryAuthDAO();   // Create databases required by registration endpoint
        MemoryUserDAO uDAO = new MemoryUserDAO();

        String name = "Joe";
        String password = "Swordfish";
        String email = "another@yahmail.com";
        String token = "";

        try {
            token = RegisterService.register(new UserData(name, password, email), uDAO, aDAO);
        } catch (BadRequestException badEx) {
            Assertions.fail("Registration failed, bad request exception thrown.");
        } catch (AlreadyTakenException alrEx) {
            Assertions.fail("Registration failed, data access exception thrown.");
        }

        // Check that all entries are in user database
        Assertions.assertEquals(uDAO.getUser(name).username(), name);
        Assertions.assertEquals(uDAO.getUser(name).password(), password);
        Assertions.assertEquals(uDAO.getUser(name).email(), email);

        // Check that auth database has new user entry
        Assertions.assertEquals(aDAO.getAuth(token).username(), name);
    }

    @Test
    @DisplayName("Check if register service rejects an incorrectly registered user")
    public void testRegisterRejectNullValues() {
        MemoryAuthDAO aDAO = new MemoryAuthDAO();   // Create databases required by registration endpoint
        MemoryUserDAO uDAO = new MemoryUserDAO();

        String name = null;     // Simulate missing name
        String password = "Swordfish";
        String email = "another@yahmail.com";

        // Test thrown exception for null username
        Assertions.assertThrows(BadRequestException.class, () ->
                RegisterService.register(new UserData(name, password, email), uDAO, aDAO));

        String newName = "Bob";   // Give name variable a non-null value
        String newMail = null;   // Make email variable null

        // Test thrown exception for null email
        Assertions.assertThrows(BadRequestException.class, () ->
                RegisterService.register(new UserData(newName, password, newMail), uDAO, aDAO));

    }

    @Test
    @DisplayName("Check if register service rejects an incorrectly registered user")
    public void testRegisterRejectTakenName() {
        MemoryAuthDAO aDAO = new MemoryAuthDAO();   // Create databases required by registration endpoint
        MemoryUserDAO uDAO = new MemoryUserDAO();

        String name = "Bob";     // Simulate missing name
        String password = "Swordfish";
        String email = "another@yahmail.com";

        try {
            RegisterService.register(new UserData(name, password, email), uDAO, aDAO);
        } catch (AlreadyTakenException alrEx) {
        } catch (BadRequestException badEx) {
        }

        // Test thrown exception for null username
        Assertions.assertThrows(AlreadyTakenException.class, () ->
                RegisterService.register(new UserData(name, password, email), uDAO, aDAO));
    }

    @Test
    @DisplayName("Check for successful login")
    public void loginSuccess() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // Create new databases
        MemoryUserDAO userDAO = new MemoryUserDAO();

        String name = "Joe";    // Username and other needed data for this test
        String password = "beans123";
        String email = "mail";
        String token = "";  // Used to check that user exists before commencing test

        // Add new user to database and make auth data
        try {
            userDAO.createUser(new UserData(name, password, email));
            token = authDAO.createAuth(name);
        } catch (AlreadyTakenException dataEx) {
            Assertions.fail("AlreayTakenException thrown.");
        }

        Assertions.assertEquals(authDAO.getAuth(token).username(), name); // Verify that auth data was added

        authDAO.clearAuthDatabase();    // Simulate a logout by clearing authTokens
        String newToken = "";            // Use to retrieve new authToken from login

        // Attempt to log same user back in
        try {
            newToken = LoginOutService.login(new UserData(name, password, email), userDAO, authDAO).authToken();
        } catch (BadRequestException badEx) {
            Assertions.fail("Login failed, bad request exception thrown.");
        } catch (UnauthorizedException unEx) {
            Assertions.fail("Login failed, unauthorized exception thrown.");
        }

        Assertions.assertEquals(authDAO.getAuth(newToken).username(), name); // Check if username is in auth database
    }

    @Test
    @DisplayName("Check for rejected login from wrong password")
    public void loginUnauthorized() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // Create new databases
        MemoryUserDAO userDAO = new MemoryUserDAO();

        String name = "Joe";    // Username and other needed data for this test
        String password = "beans123";
        String wrongWord = "321beans";
        String email = "mail";

        // Add new user to database and make auth data
        try {
            userDAO.createUser(new UserData(name, password, email));
            authDAO.createAuth(name);
        } catch (AlreadyTakenException dataEx) {
            Assertions.fail("AlreayTakenException thrown.");
        }

        authDAO.clearAuthDatabase();    // Simulate a logout by clearing authTokens

        // Attempt to log same user back in
        try {
            LoginOutService.login(new UserData(name, password, email), userDAO, authDAO);
        } catch (BadRequestException badEx) {
            Assertions.fail("Login failed, bad request exception thrown.");
        } catch (UnauthorizedException unEx) {
            Assertions.fail("Login failed, unauthorized exception thrown.");
        }

        // Should throw unauthorized exception when given wrong password
        Assertions.assertThrows(UnauthorizedException.class, () ->
                LoginOutService.login(new UserData(name, wrongWord, email), userDAO, authDAO));
    }
    @Test
    @DisplayName("Check for successful logout")
    public void logoutSuccess() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // Make an auth database
        String name = "Benjamin";
        String token = authDAO.createAuth(name);        // Add new auth data and retrieve token

        Assertions.assertEquals(authDAO.getAuth(token).username(), name);   // Verify if auth data was made

        // Attempt logout
        try {
            LoginOutService.logout(token, authDAO);
        } catch (UnauthorizedException unEx) {
            Assertions.fail("Logout test failed.");
        }
        Assertions.assertTrue(authDAO.getAuthDataTable().isEmpty());    // Check that auth data is gone
    }

    @Test
    @DisplayName("Test failed logout")
    public void logoutWithWrongAuthToken() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // Make an auth database
        String name = "Benjamin";
        String wrongToken = "3aWRONG-TOKEN9tr4k";       // Make a token with wrong format and value
        String correctToken = authDAO.createAuth(name); // Add new auth data and retrieve correct token

        Assertions.assertEquals(authDAO.getAuth(correctToken).username(), name);   // Verify if auth data was made

        // Attempt logout with incorrect authToken
        Assertions.assertThrows(UnauthorizedException.class, () -> LoginOutService.logout(wrongToken, authDAO));
    }

    @Test
    @DisplayName("Test successful request for listGames service")
    public void listGamesSuccess() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // Make an auth database
        MemoryGameDAO gameDAO = new MemoryGameDAO();    // Make games database
        gameDAO.createGame("ChessGame1");       // Create chess game
        String name = "Benjamin";                       // Start making new auth data
        String correctToken = authDAO.createAuth(name); // Add new auth data and retrieve correct token
        Assertions.assertEquals(authDAO.getAuth(correctToken).username(), name);   // Verify if auth data was made

        Collection<GameData> listOfGames = GameService.getGames(gameDAO);   // Retrieve games
        Assertions.assertTrue(!listOfGames.isEmpty());  // Verify that the container is not empty
    }

    @Test
    @DisplayName("Test listGames rejection from wrong authToken")
    public void listGamesWrongAuthToken() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();    // Make an auth database

        //FIXME:
        // Add negative case test
    }

    @Test
    @DisplayName("Test successfully created game")
    public void createGameServiceSuccess() {
        MemoryGameDAO gameDAO = new MemoryGameDAO();    // Create games database
        int gameID = 0;
        try {
            gameID = GameService.createGame("Game1", gameDAO); // Create a game
        } catch (DataAccessException dataEx) {
            Assertions.fail("Error: failed to create game");
        } catch (BadRequestException badEx) {
            Assertions.fail("Error: bad request");
        }

        // Verify that game was created and returned a nonzero id
        Assertions.assertTrue(!gameDAO.getGameDatabase().isEmpty());
        Assertions.assertTrue(gameID != 0);
    }

    @Test
    @DisplayName("Test for when name parameter is missing data")
    public void createGameFailureTest() {
        MemoryGameDAO gameDAO = new MemoryGameDAO();    // Create games database
        String gameName = null;
        Assertions.assertThrows(BadRequestException.class, () -> GameService.createGame(gameName, gameDAO));
    }



    // TODO:
    //  listGames() positive and negative tests
    //  createGame() positive and negative tests
    //  joinGame() positive and negative tests

}
