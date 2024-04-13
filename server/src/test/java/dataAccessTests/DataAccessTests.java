package dataAccessTests;

import chess.ChessGame;
import config.ConfigConsts;
import dataAccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import exception.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import java.sql.SQLException;
import java.util.Collection;
import java.util.ArrayList;

public class DataAccessTests {

    @Test
    @DisplayName("Test clear game DAO")
    public void testGameDaoClear() {
        SQLGameDAO gDao = new SQLGameDAO();
        String name = "JohnnyJohnWillyBartson";
        try {
            int id = gDao.createGame(name);
            Assertions.assertTrue(gDao.hasGame(id));
            gDao.clearGamesDataBase();
            Assertions.assertTrue(gDao.isEmpty());
        } catch (SQLException | DataAccessException e) {
            Assertions.fail("SQL exception.");
        }
    }

    @Test
    @DisplayName("Test create game")
    public void testGameDaoCreateGame() {
        SQLGameDAO gDao = new SQLGameDAO();
        String name = "JohnnyJohnWillyBartson";
        try {
            int id = gDao.createGame(name);
            Assertions.assertTrue(gDao.hasGame(id));
            gDao.clearGamesDataBase();
        } catch (SQLException e) {
            Assertions.fail("SQL exception.");
        }
    }

    @Test
    @DisplayName("Test create game")
    public void testGameDaoCreateGameDifferentId() {
        SQLGameDAO gDao = new SQLGameDAO();
        String name = "JohnnyJohnWillyBartson";
        try {
            int id = gDao.createGame(name);
            int otherId = gDao.createGame(name);
            Assertions.assertFalse(id == otherId);
            gDao.clearGamesDataBase();
        } catch (SQLException e) {
            Assertions.fail("SQL exception.");
        }
    }

    @Test
    @DisplayName("Test update white name")
    public void testUpdateWhite() {
        SQLGameDAO gDao = new SQLGameDAO();
        String name = "JohnnyJohnWillyBartson";
        try {
            int id = gDao.createGame(name);
            Assertions.assertTrue(gDao.hasAvailableTeam(id, ConfigConsts.WHITE_TEAM_COL)); // Should indicate team is vacant
            gDao.updateWhiteUsername(id, name);
            gDao.clearGamesDataBase();
        } catch (SQLException | AlreadyTakenException e) {
            Assertions.fail("SQL exception.");
        } catch (DataAccessException dataEx) {
            Assertions.fail("Error in checking white team for name.");
        }
    }

    @Test
    @DisplayName("Test that DAO indicates correct vacancy of white team")
    public void testUpdateWhiteUserIsTaken() {
        SQLGameDAO gDao = new SQLGameDAO();
        String name = "JohnnyJohnWillyBartson";
        try {
            int id = gDao.createGame(name);
            gDao.updateWhiteUsername(id, name);
            Assertions.assertFalse(gDao.hasAvailableTeam(id, ConfigConsts.WHITE_TEAM_COL)); // Should indicate team is taken
        } catch (SQLException | AlreadyTakenException e) {
            Assertions.fail("SQL exception.");
        } catch (DataAccessException dataEx) {
            Assertions.fail("Error in checking white team for name.");
        }
        gDao.clearGamesDataBase(); // Reset games table
    }

    @Test
    @DisplayName("Test update black name")
    public void testUpdateBlack() {
        SQLGameDAO gDao = new SQLGameDAO();
        String name = "JohnnyJohnWillyBartson";
        try {
            int id = gDao.createGame(name);
            Assertions.assertTrue(gDao.hasAvailableTeam(id, ConfigConsts.BLACK_TEAM_COL));
            gDao.updateBlackUsername(id, name);
            gDao.clearGamesDataBase();
        } catch (SQLException | AlreadyTakenException e) {
            Assertions.fail("SQL exception.");
        } catch (DataAccessException dataEx) {
            Assertions.fail("Error in checking white team for name.");
        }
    }

    @Test
    @DisplayName("Test vacancy indication of black team")
    public void testBlackTeamTaken() {
        SQLGameDAO gDao = new SQLGameDAO();
        String name = "JohnnyJohnWillyBartson";
        try {
            int id = gDao.createGame(name);
            gDao.updateBlackUsername(id, name);
            Assertions.assertFalse(gDao.hasAvailableTeam(id, ConfigConsts.BLACK_TEAM_COL));
            gDao.clearGamesDataBase();
        } catch (SQLException | AlreadyTakenException e) {
            Assertions.fail("SQL exception.");
        } catch (DataAccessException dataEx) {
            Assertions.fail("Error in checking white team for name.");
        }
    }

    @Test
    @DisplayName("Test isEmpty method for AuthDAO")
    public void testAuthDaoEmpty() {
        SQLAuthDAO aDao = new SQLAuthDAO();
        try {
            aDao.clearAuthDatabase();
            Assertions.assertTrue(aDao.isEmpty());
        } catch (DataAccessException e) {
            Assertions.fail("SQL exception.");
        }
    }

    @Test
    @DisplayName("Test isEmpty method for GameDAO")
    public void testGameDaoEmpty() {
        SQLGameDAO gDao = new SQLGameDAO();
        try {
            gDao.clearGamesDataBase();
            Assertions.assertTrue(gDao.isEmpty());
        } catch (DataAccessException e) {
            Assertions.fail("SQL exception.");
        }
    }

    @Test
    @DisplayName("Test listGames from GameDAO")
    public void testListGames() {
        SQLGameDAO gDao = new SQLGameDAO();

        try {
            gDao.createGame("Game1");
            ArrayList<GameData> games = gDao.getGamesList();
            Assertions.assertFalse(games.isEmpty());
            gDao.clearGamesDataBase();
        } catch (SQLException e) {
            Assertions.fail("SQL exception thrown.");
        }
    }

    @Test
    @DisplayName("Test listGames from GameDAO when empty")
    public void testListGamesEmpty() {
        SQLGameDAO gDao = new SQLGameDAO();

        try {
            ArrayList<GameData> games = gDao.getGamesList();
            Assertions.assertTrue(games.isEmpty());
            gDao.clearGamesDataBase();
        } catch (SQLException e) {
            Assertions.fail("SQL exception thrown.");
        }
    }

    @Test
    @DisplayName("Test hasGame key-checking method in GameDAO")
    public void testHasGame() {
        SQLGameDAO gDao = new SQLGameDAO();

        try {
            int id = gDao.createGame("NewGame");    // Make game, retrieve id
            Assertions.assertTrue(gDao.hasGame(id));        // Verify game can be verified with id
            gDao.clearGamesDataBase();
        } catch (SQLException e) {
            Assertions.fail("SQL exception thrown.");
        }
    }

    @Test
    @DisplayName("Test hasGame key-checking method in GameDAO for nonexistent game")
    public void testDoesNotHaveGame() {
        SQLGameDAO gDao = new SQLGameDAO();

        try {
            gDao.createGame("NewGame");    // Make game, retrieve id
            int wrongId = -1;       // Wrong id will not yield a game
            Assertions.assertFalse(gDao.hasGame(wrongId));        // Verify game can be verified with id
            gDao.clearGamesDataBase();
        } catch (SQLException e) {
            Assertions.fail("SQL exception thrown.");
        }
    }


    @Test
    @DisplayName("Test clear auth DAO")
    public void testAuthDaoClear() {
        SQLAuthDAO aDao = new SQLAuthDAO();
        String name = "JohnnyJohnWillyBartson";
        try {
            String token = aDao.createAuth(name);
            Assertions.assertTrue(aDao.hasAuth(token));
            aDao.clearAuthDatabase();
            Assertions.assertTrue(aDao.isEmpty());
        } catch (SQLException | DataAccessException e) {
            Assertions.fail("SQL exception.");
        }
    }



    @Test
    @DisplayName("Test clear user DAO")
    public void testUserDaoClear() {
        SQLUserDAO uDao = new SQLUserDAO();
        String name = "JohnnyJohnWillyBartson";
        try {
            uDao.createUser(new UserData(name, "password", "email"));
            Assertions.assertTrue(uDao.hasThisUsername(name));
            uDao.clearUserDatabase();
            Assertions.assertFalse(uDao.hasThisUsername(name));
        } catch (SQLException e) {
            Assertions.fail("SQL exception.");
        }
    }

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

        String deleteUserStmt = "DELETE FROM " + ConfigConsts.USER_TABLE_NAME + " WHERE username =?";
        String deleteAuthStmt = "DELETE FROM " + ConfigConsts.AUTH_TABLE_NAME + " WHERE authToken =?";


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

        // When checking password, use hashed password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Check that all entries are in user database
        Assertions.assertEquals(daos.sqlUserDAO.getUser(name).username(), name);
        Assertions.assertTrue(encoder.matches(password, daos.sqlUserDAO.getUser(name).password()));
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
        String deleteUserStmt = "DELETE FROM " + ConfigConsts.USER_TABLE_NAME + " WHERE username =?";
        String deleteAuthStmt = "DELETE FROM " + ConfigConsts.AUTH_TABLE_NAME + " WHERE authToken =?";

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
        aDao.clearAuthDatabase(); // Reset tables
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
        aDao.clearAuthDatabase(); // Reset tables
        uDao.clearUserDatabase();
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
        aDao.clearAuthDatabase(); // reset auth table
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
        aDao.clearAuthDatabase(); // Reset auth table
    }

    @Test
    @DisplayName("Test successful request for listGames service")
    public void listGamesSuccess() {
        SQLGameDAO gDao = new SQLGameDAO();   // Make new DAOs
        SQLAuthDAO aDao = new SQLAuthDAO();
        try {
            gDao.createGame("ChessGame1");       // Create chess game
            String name = "Benjamin";                       // Start making new auth data
            String correctToken = aDao.createAuth(name); // Add new auth data and retrieve correct token
            Assertions.assertEquals(aDao.getAuth(correctToken).username(), name);   // Verify auth data creation

            Collection<GameData> listOfGames = GameService.getGames(gDao);   // Retrieve games

            Assertions.assertTrue(!listOfGames.isEmpty());  // Verify that the container is not empty
        } catch (SQLException | DataAccessException e) {
            Assertions.fail("Failure.");
        }
        gDao.clearGamesDataBase(); // Clear tables
        aDao.clearAuthDatabase();
    }

    @Test
    @DisplayName("Test listGames rejection from wrong authToken")
    public void listGamesWrongAuthToken() {
        SQLAuthDAO aDao = new SQLAuthDAO();    // Make an auth database
        SQLGameDAO gameDAO = new SQLGameDAO();    // Make games database
        try {
            gameDAO.createGame("Game1");            // Create game and add to database
            String token = aDao.createAuth("Bob");   // Add auth data and get token
            String wrongToken = "notRealToken";             // Make a fake token

            ArrayList<GameData> games = new ArrayList<>();

            // Will NOT retrieve games if auth database doesn't have authToken.
            if (aDao.hasAuth(wrongToken)) {
                games = GameService.getGames(gameDAO);
                Assertions.fail("Error: retrieved games with incorrect authToken");
            }
            Assertions.assertTrue(games.isEmpty()); // Should be empty
        } catch (SQLException e) {
            Assertions.fail("SQL exception.");
        }
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
        gDao.clearGamesDataBase(); // Reset games table
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

            Assertions.assertTrue(gDao.hasAvailableTeam(gameID, ConfigConsts.WHITE_TEAM_COL));

            gDao.updateWhiteUsername(gameID, userName);      // Add user to white team of chess game

            Assertions.assertFalse(gDao.hasAvailableTeam(gameID, ConfigConsts.WHITE_TEAM_COL));
        } catch (DataAccessException | SQLException e) {
            Assertions.fail("SQL error.");
        } catch (AlreadyTakenException alrEx) {
            Assertions.fail("Failed: already taken exception thrown.");
        }
        gDao.clearGamesDataBase(); // Reset games table
        aDao.clearAuthDatabase(); // Reset auth table
    }


    @Test
    @DisplayName("Test alreadyTaken exception for joinGame request")
    public void joinWhiteTeamReject() {
        SQLGameDAO gDao = new SQLGameDAO();    // New tables
        SQLAuthDAO aDao = new SQLAuthDAO();
        String gameName = "Game1";                      // Name for new chess game
        String userName = "Freddy";                     // Username for client
        String otherUser = "Bonnie";                    // User trying to join already-taken team
        ChessGame.TeamColor requestedTeam = ChessGame.TeamColor.WHITE; // Team that second user will try to join
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
        gDao.clearGamesDataBase(); // Reset game table
        aDao.clearAuthDatabase(); // Reset auth table
    }



}
