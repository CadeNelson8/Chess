package client;

import dataaccess.sql.DatabaseManager;
import dataaccess.sql.SqlAuthDataAccess;
import dataaccess.sql.SqlGameDataAccess;
import dataaccess.sql.SqlUserDataAccess;
import org.junit.jupiter.api.*;
import server.Server;
import service.AuthService;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private UserService userService;

    private GameService gameService;
    SqlUserDataAccess sqlUserDA = new SqlUserDataAccess();
    SqlAuthDataAccess sqlAuth = new SqlAuthDataAccess();
    SqlGameDataAccess sqlGame = new SqlGameDataAccess();
    private static ServerFacade facade = null;
    private static Server server;
    AuthService authService = new AuthService(sqlAuth);


    @BeforeEach
    void setUp() {
        GameService gameService = new GameService(sqlGame, authService);
        this.gameService = gameService;
        DatabaseManager.createDatabase();
        UserService userService = new UserService(sqlUserDA, authService);
        this.userService = userService;
    }
    @AfterEach
    void clean(){
        gameService.clearRequest();
        userService.clearRequest();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    /*@Test
    void register() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }*/

}
