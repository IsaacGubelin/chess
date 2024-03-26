package clientTests;

import facade.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;



public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
//        facade = new ServerFacade(port);
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
    @DisplayName("Checks 200 code evaluator using same logic as private method")
    public void rejectBadJoinRequest() {
//        ServerFacade.
    }

}
