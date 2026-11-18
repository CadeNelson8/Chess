package client;

import dataaccess.sql.DatabaseManager;
import dataaccess.sql.SqlAuthDataAccess;
import dataaccess.sql.SqlGameDataAccess;
import dataaccess.sql.SqlUserDataAccess;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.AuthService;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server = null;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:"+port);
    }

    @AfterAll
    static void stopServer() {
        facade.executeRequest(null, "delete", "/db", false);
        server.stop();
    }


    @Test
    void register() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        facade.executeRequest(user, "post", "/user", true);
        assertTrue(true);
    }

}
