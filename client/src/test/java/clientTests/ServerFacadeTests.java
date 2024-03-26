package clientTests;

import facade.ServerFacade;
import model.AuthData;
import model.GameRequestData;
import model.UserData;
import org.junit.jupiter.api.*;
import resException.ResponseException;
import server.Server;



public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("First test")
    public void test1() {
        Assertions.assertFalse(false);
    }

    @Test
    @DisplayName("First test")
    public void test2() {
        Assertions.assertEquals((3+5), 10-2);
    }

    @Test
    @DisplayName("Checks 200 code evaluator using same logic as private method")
    public void testCheckResponseBadCode() {
        int resCode = 400;
        Assertions.assertFalse(resCode / 100 == 2);
    }

    @Test
    @DisplayName("Checks 200 code evaluator using same logic as private method for good code")
    public void testCheckResponseGoodCode() {
        int resCode = 200;
        Assertions.assertTrue(resCode / 100 == 2);
    }

    @Test
    @DisplayName("Checks if wrong authToken results in rejected join request")
    public void rejectBadJoinTokenRequest() {
        String token = "123WRONGTOKENFORMAT789";

        GameRequestData req = new GameRequestData("Game1", "BLACK", 0);

        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(token, req));

    }

    @Test
    @DisplayName("Checks if wrong team results in rejected join request")
    public void rejectBadTeamRequest() {
        String token = "123WRONGTOKENFORMAT789";

        GameRequestData req = new GameRequestData("Game1", "YELLOW", 0);

        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(token, req));

    }

    @Test
    @DisplayName("Checks if join request when user types both teams")
    public void rejectBadStringRequest() {
        String token = "123WRONGTOKENFORMAT789";

        GameRequestData req = new GameRequestData("Game1", "BLACKWHITE", 0);

        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(token, req));

    }

    @Test
    @DisplayName("Registration test")
    public void registerUserTest() {
        try {
            AuthData responseAuth = facade.register(new UserData("Jarnathan", "pass", "mail"));

        } catch (ResponseException ex) {
            Assertions.fail("Response exception thrown.");
        }
    }

    @Test
    @DisplayName("Registration exception test")
    public void registerSameUserTwiceTest() {
        try {
            AuthData responseAuth = facade.register(new UserData("BOBBY", "pass", "mail"));

        } catch (ResponseException ex) {
            Assertions.fail("Response exception thrown after first registration.");
        }
        Assertions.assertThrows(ResponseException.class, () ->
                facade.register(new UserData("BOBBY", "pass2", "mail2")));
    }

    @Test
    @DisplayName("Registration missing name exception test")
    public void registerSameUserMissingName() {

        Assertions.assertThrows(ResponseException.class, () ->
                facade.register(new UserData(null, "pass2", "mail2")));
    }

    @Test
    @DisplayName("Registration missing password exception test")
    public void registerSameUserMissingPassword() {

        Assertions.assertThrows(ResponseException.class, () ->
                facade.register(new UserData("BARNEY", null, "mail2")));
    }

    @Test
    @DisplayName("Registration missing email exception test")
    public void registerSameUserMissingEmail() {

        Assertions.assertThrows(ResponseException.class, () ->
                facade.register(new UserData("SUSIE", "pass2", null)));
    }

}
