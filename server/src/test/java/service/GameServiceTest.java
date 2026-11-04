package service;

import dataaccess.*;
import dataaccess.sql.DatabaseManager;
import dataaccess.sql.sqlAuthDataAccess;
import dataaccess.sql.sqlGameDataAccess;
import dataaccess.sql.sqlUserDataAccess;
import io.javalin.Javalin;
import model.AuthData;
import model.GameName;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Color;
import server.GameRequest;

import static dataaccess.sql.DatabaseManager.createDatabase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTest {
    private UserService userService;
    private Javalin javalin;
    private GameService gameService;
    sqlUserDataAccess sqlUserDA = new sqlUserDataAccess();
    sqlAuthDataAccess sqlAuth = new sqlAuthDataAccess();
    sqlGameDataAccess sqlGame = new sqlGameDataAccess();
    AuthService authService = new AuthService(sqlAuth);

    @BeforeEach
    void setUp() {
        GameService gameService = new GameService(sqlGame, authService);
        UserService userService = new UserService(sqlUserDA, authService);
        this.userService = userService;
        this.gameService = gameService;
        createDatabase();
        DatabaseManager.createDatabase();
        UserDataAccess userDA = new UserDataAccess();
        AuthDataAccess authDA = new AuthDataAccess();
        GameDataAccess gameDA = new GameDataAccess();
        AuthService authService = new AuthService(authDA);
    }

    @AfterEach
    void cleanup(){
        gameService.clearRequest();
        userService.clearRequest();
    }


    @Test
    void registerRequestSuccess() throws AlreadyTakenException {
        UserData user = new UserData("me", "1234", "I@mail.com");
        AuthData auth = userService.registerRequest(user);

        assertEquals("me", auth.username());
    }

    @Test
    void registerRequestFail() throws AlreadyTakenException {
        UserData user = new UserData("me", "1234", "I@mail.com");
        AuthData auth = userService.registerRequest(user);
        assertThrows(AlreadyTakenException.class, () -> {userService.registerRequest(user);});
    }

    @Test
    void clearDbPass(){
        userService.clearRequest();
        gameService.clearRequest();
    }

    @Test
    void loginRequestSuccess() throws AlreadyTakenException {
        UserData user = new UserData("me", "1234", "I@mail.com");
        AuthData auth = userService.registerRequest(user);
        AuthData authData = userService.loginRequest(user);

        assertEquals("me", authData.username());
    }

    @Test
    void loginRequestFail() throws UnauthorizedException, AlreadyTakenException {
        UserData user = new UserData("me", "1234", "I@mail.com");
        assertThrows(UnauthorizedException.class, () -> {userService.loginRequest(user);});
    }

    @Test
    void logoutRequestSuccess() throws AlreadyTakenException {
        UserData user = new UserData("me", "1234", "I@mail.com");
        AuthData auth = userService.registerRequest(user);
        AuthData authData = userService.loginRequest(user);
        userService.logoutRequest(auth.authToken());

        assertEquals(authData.username(), "me");
    }

    @Test
    void logoutRequestFail() {
        String auth = "nope";
        assertThrows(UnauthorizedException.class, () -> userService.logoutRequest(auth));
    }

    @Test
    void createGameSuccess() throws AlreadyTakenException {
        UserData user = new UserData("me", "1234", "I@mail.com");
        AuthData auth = userService.registerRequest(user);
        GameName newgame = new GameName("hi");
        int id = gameService.createGameRequest(newgame.gameName(), auth.authToken());

        assertEquals(1, id);
    }

    @Test
    void createGameFail() throws AlreadyTakenException, UnauthorizedException {
        UserData user = new UserData("me", "1234", "I@mail.com");
        AuthData auth = userService.registerRequest(user);
        GameName newgame = new GameName("hi");
        assertThrows(UnauthorizedException.class, () -> {gameService.createGameRequest(newgame.gameName(), "nope");});
    }

    @Test
    void joinGameSuccess() throws AlreadyTakenException {
        UserData user = new UserData("me", "1234", "I@mail.com");
        AuthData auth = userService.registerRequest(user);
        GameName newgame = new GameName("hi");
        int id = gameService.createGameRequest(newgame.gameName(), auth.authToken());
        GameRequest enter = new GameRequest(Color.BLACK, id);
        gameService.joinGameRequest(enter, auth.authToken());

    }

    @Test
    void joinGameFail() throws AlreadyTakenException {
        UserData user = new UserData("me", "1234", "I@mail.com");
        AuthData auth = userService.registerRequest(user);
        GameName newgame = new GameName("hi");
        int id = gameService.createGameRequest(newgame.gameName(), auth.authToken());
        GameRequest enter = new GameRequest(Color.BLACK, id);
        gameService.joinGameRequest(enter, auth.authToken());

        assertThrows(AlreadyTakenException.class, () -> {gameService.joinGameRequest(enter, "nope");});
    }
}
