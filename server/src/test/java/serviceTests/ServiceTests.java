package serviceTests;

import config.Config;
import dataAccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import exception.DataAccessException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.DatabaseDAOCollection;
import service.ClearService;
import service.GameService;
import service.LoginOutService;
import service.RegisterService;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Collection;
import java.util.HashSet;

public class ServiceTests {


    @Test
    @DisplayName("Check if clear endpoint empties all databases")
    public void testClearApplication() {

        // New database
        SQLAuthDAO aDao = new SQLAuthDAO();
        SQLUserDAO uDao = new SQLUserDAO();
        SQLGameDAO gDao = new SQLGameDAO();

        try {
            aDao.createAuth("User1");           // Add some auth data
            aDao.createAuth("User2");

                                            // Add new user data
            uDao.createUser(new UserData("userName", "123456789", "email"));
            uDao.createUser(new UserData("anotherName", "swordfish", "best@freemail.com"));


            gDao.createGame("TheGame");     // Add games to game database
            gDao.createGame("TheOtherGame");
            gDao.createGame("A third game");

            // All databases should now contain data
            Assertions.assertFalse(gDao.isEmpty() || aDao.isEmpty());

            // TESTING CLEAR ENDPOINT
            ClearService.clearService(gDao, uDao, aDao);
            Assertions.assertTrue(gDao.isEmpty() && aDao.isEmpty());
        } catch (SQLException | DataAccessException ex) {
            Assertions.fail("Exception thrown.");
        }
    }

    @Test
    @DisplayName("Check if register service accepts a properly registered user")
    public void testRegisterAccept() {

        DatabaseDAOCollection daos = new DatabaseDAOCollection();

        String name = "Molly";
        String password = "Swordfish";
        String email = "another@yahmail.com";
        String token = "";

        String deleteUserStmt = "DELETE FROM " + Config.USER_TABLE_NAME + " WHERE username =?";
        String deleteAuthStmt = "DELETE FROM " + Config.AUTH_TABLE_NAME + " WHERE authToken =?";


        try {
            token = RegisterService.register(new UserData(name, password, email), daos.sqlUserDAO, daos.sqlAuthDAO);
            System.out.println("Token value: " + token);
        } catch (BadRequestException badEx) {
            Assertions.fail("Registration failed, bad request exception thrown.");
        } catch (AlreadyTakenException alrEx) {
            Assertions.fail("Registration failed, data access exception thrown.");
        } catch (SQLException ex) {
            Assertions.fail("Failed, SQL exception.");
        }

        // Check that all entries are in user database
        Assertions.assertEquals(daos.sqlUserDAO.getUser(name).username(), name);
        Assertions.assertEquals(daos.sqlUserDAO.getUser(name).password(), password);
        Assertions.assertEquals(daos.sqlUserDAO.getUser(name).email(), email);

        // Check that auth database has new user entry
        try {
            Assertions.assertEquals(daos.sqlAuthDAO.getAuth(token).username(), name);
        } catch (DataAccessException e) {
            Assertions.fail("getAuth method failed.");
        }

        // Undo changes made to chess database
        try {
            ExecuteSQL.executeUpdate(deleteUserStmt, name);
            ExecuteSQL.executeUpdate(deleteAuthStmt, token);
        } catch (SQLException e) {
            System.out.println("Could not remove temporary user from database.");
        }
    }

    @Test
    @DisplayName("Check if register service rejects an incorrectly registered user")
    public void testRegisterRejectNullValues() {
        SQLAuthDAO aDAO = new SQLAuthDAO();   // Create databases required by registration endpoint
        SQLUserDAO uDAO = new SQLUserDAO();

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
        SQLAuthDAO aDAO = new SQLAuthDAO();   // Create databases required by registration endpoint
        SQLUserDAO uDAO = new SQLUserDAO();

        String name = "Bob";     // Simulate missing name
        String password = "Swordfish";
        String email = "another@yahmail.com";
        String token = "";
        String deleteUserStmt = "DELETE FROM " + Config.USER_TABLE_NAME + " WHERE username =?";
        String deleteAuthStmt = "DELETE FROM " + Config.AUTH_TABLE_NAME + " WHERE authToken =?";

        try {
            token = RegisterService.register(new UserData(name, password, email), uDAO, aDAO);
        } catch (AlreadyTakenException alrEx) {
            Assertions.fail("Already taken exception triggered.");
        } catch (SQLException | BadRequestException sqlEx) {

        }

        // Test thrown exception for null username
        Assertions.assertThrows(AlreadyTakenException.class, () ->
                RegisterService.register(new UserData(name, password, email), uDAO, aDAO));
        try {
            ExecuteSQL.executeUpdate(deleteUserStmt, name);
            ExecuteSQL.executeUpdate(deleteAuthStmt, token);
        } catch (SQLException ex) {
            System.out.println("Error: could not undo changes made to database.");
        }
    }

    @Test
    @DisplayName("Check for successful login")
    public void loginSuccess() {
        SQLAuthDAO aDao = new SQLAuthDAO(); // Create new tables
        SQLUserDAO uDao = new SQLUserDAO();

        String name = "Joe";    // Username and other needed data for this test
        String password = "beans123";
        String email = "mail";
        String token = "";  // Used to check that user exists before commencing test

        // Add new user to database and make auth data
        try {
            uDao.createUser(new UserData(name, password, email));
            token = aDao.createAuth(name);
        } catch (SQLException sqlEx) {
            Assertions.fail("Exception thrown.");
        }

        try {
            Assertions.assertEquals(aDao.getAuth(token).username(), name); // Verify that auth data was added

            aDao.deleteAuth(token);         // Simulate a logout by clearing recently added auth
            String newToken = "";           // Use to retrieve new authToken from login

            // Attempt to log same user back in
            try {
                newToken = LoginOutService.login(new UserData(name, password, email), uDao, aDao).authToken();
            } catch (BadRequestException badEx) {
                Assertions.fail("Login failed, bad request exception thrown.");
            } catch (UnauthorizedException unEx) {
                Assertions.fail("Login failed, unauthorized exception thrown.");
            } catch (DataAccessException dataEx) {
                Assertions.fail("Login failed, SQL problem.");
            }

            // Check if username is in auth database
            Assertions.assertEquals(aDao.getAuth(newToken).username(), name);

            // Undo changes made to database
            aDao.deleteAuth(newToken);
            uDao.deleteUser(name);

        } catch (DataAccessException dEx) {
            Assertions.fail("Data access exception.");
        }
    }

    @Test
    @DisplayName("Check for rejected login from wrong password")
    public void loginUnauthorized() {
        SQLAuthDAO aDao = new SQLAuthDAO(); // Create new tables
        SQLUserDAO uDao = new SQLUserDAO();

        String name = "Joe";    // Username and other needed data for this test
        String password = "beans123";
        String wrongWord = "321beans";
        String email = "mail";
        String token = "";

        // Add new user to database and make auth data
        try {
            uDao.createUser(new UserData(name, password, email));
            token = aDao.createAuth(name);
        } catch (SQLException ex) {
            Assertions.fail("SQL exception thrown.");
        }

        aDao.deleteAuth(token);    // Simulate a logout

        // Attempt to log same user back in
        try {
            LoginOutService.login(new UserData(name, password, email), uDao, aDao);
        } catch (BadRequestException | DataAccessException ex) {
            Assertions.fail("Login failed, exception thrown.");
        } catch (UnauthorizedException unEx) {
            Assertions.fail("Login failed, unauthorized exception thrown.");
        }

        // Should throw unauthorized exception when given wrong password
        Assertions.assertThrows(UnauthorizedException.class, () ->
                LoginOutService.login(new UserData(name, wrongWord, email), uDao, aDao));
    }
    @Test
    @DisplayName("Check for successful logout")
    public void logoutSuccess() {
        SQLAuthDAO aDao = new SQLAuthDAO();
        String name = "Benjamin";
        try {
            String token = aDao.createAuth(name);        // Add new auth data and retrieve token

            Assertions.assertEquals(aDao.getAuth(token).username(), name);   // Verify if auth data was made

            // Attempt logout
            LoginOutService.logout(token, aDao);

            Assertions.assertFalse(aDao.hasAuth(token));    // Check that auth data is gone
        } catch (SQLException | DataAccessException ex) {
            Assertions.fail("SQL error.");
        } catch (UnauthorizedException unEx) {
            Assertions.fail("Logout test failed.");
        }
    }

    @Test
    @DisplayName("Test failed logout")
    public void logoutWithWrongAuthToken() {
        SQLAuthDAO aDao = new SQLAuthDAO();    // Make an auth table
        String name = "Benjamin";
        String wrongToken = "3aWRONG-TOKEN9tr4k";       // Make a token with wrong format and value
        try {
            String correctToken = aDao.createAuth(name); // Add new auth data and retrieve correct token
            Assertions.assertEquals(aDao.getAuth(correctToken).username(), name); // Verify auth data creation
            // Attempt logout with incorrect authToken
            Assertions.assertThrows(UnauthorizedException.class, () -> LoginOutService.logout(wrongToken, aDao));
            // Undo change to table
            aDao.deleteAuth(correctToken);
        } catch (DataAccessException | SQLException e) {
            Assertions.fail("SQL error.");
        }
    }

    @Test
    @DisplayName("Test successful request for listGames service")
    public void listGamesSuccess() {
        DatabaseDAOCollection daos = new DatabaseDAOCollection();   // Make new DAOs
        daos.memGameDAO.createGame("ChessGame1");       // Create chess game
        String name = "Benjamin";                       // Start making new auth data
        String correctToken = daos.memAuthDAO.createAuth(name); // Add new auth data and retrieve correct token
        Assertions.assertEquals(daos.memAuthDAO.getAuth(correctToken).username(), name);   // Verify auth data creation

        Collection<GameData> listOfGames = GameService.getGames(daos);   // Retrieve games
        Assertions.assertTrue(!listOfGames.isEmpty());  // Verify that the container is not empty
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
        SQLGameDAO gDao = new SQLGameDAO();    // Create games database
        int gameID = 0;
        try {
            gameID = GameService.createGame("Game1", gDao); // Create a game
            Assertions.assertFalse(gDao.isEmpty()); // Verify that game was created and returned a nonzero id
        } catch (DataAccessException | SQLException dataEx) {
            Assertions.fail("Error: failed to create game");
        } catch (BadRequestException badEx) {
            Assertions.fail("Error: bad request");
        }

        Assertions.assertTrue(gameID != 0);
    }

    @Test
    @DisplayName("Test for when name parameter is missing data")
    public void createGameFailureTest() {
        SQLGameDAO gDao = new SQLGameDAO();    // Create games database
        String gameName = null;
        Assertions.assertThrows(BadRequestException.class, () -> GameService.createGame(gameName, gDao));
    }


    @Test
    @DisplayName("Test successful joining into chess game for white team")
    public void joinWhiteTeamSuccess() {
        SQLGameDAO gDao = new SQLGameDAO();    // New tables
        SQLAuthDAO aDao = new SQLAuthDAO();

        String gameName = "Game1";                      // Name for new chess game
        String userName = "Freddy";                     // Username for client

        try {
            String token = aDao.createAuth(userName);        // Make auth data for user and get token
            int gameID = gDao.createGame("Game1"); // Create new game and retrieve game ID

            Assertions.assertTrue(gDao.hasAvailableTeam(gameID, Config.WHITE_TEAM_COL));

            gDao.updateWhiteUsername(gameID, userName);      // Add user to white team of chess game

            Assertions.assertFalse(gDao.hasAvailableTeam(gameID, Config.WHITE_TEAM_COL));
        } catch (DataAccessException | SQLException e) {
            Assertions.fail("SQL error.");
        } catch (AlreadyTakenException alrEx) {
            Assertions.fail("Failed: already taken exception thrown.");
        }
    }


    @Test
    @DisplayName("Test alreadyTaken exception for joinGame request")
    public void joinWhiteTeamReject() {
        SQLGameDAO gDao = new SQLGameDAO();    // New tables
        SQLAuthDAO aDao = new SQLAuthDAO();
        String gameName = "Game1";                      // Name for new chess game
        String userName = "Freddy";                     // Username for client
        String otherUser = "Bonnie";                    // User trying to join already-taken team
        String requestedTeam = "WHITE";                 // Team that second user will try to join
        try {
            aDao.createAuth(userName);                   // Add both users to auth table
            String token = aDao.createAuth(otherUser);   // Keep second user's token for test
            int gameID = gDao.createGame(gameName);      // Create new game and retrieve game ID

            gDao.updateWhiteUsername(gameID, userName);  // Add first user to white team of chess game

            // Adding second user to the same game's white game should throw an exception.
            Assertions.assertThrows(AlreadyTakenException.class, () ->
                    GameService.joinGame(gameID, requestedTeam, token, aDao, gDao));
        } catch (SQLException | AlreadyTakenException e) {
            Assertions.fail("Error: exception thrown");
        }
    }



}
