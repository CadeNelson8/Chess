package service;

import dataaccess.*;
import model.AuthData;
import model.GameName;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Color;
import server.GameRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTest {
    private UserDataAccess userStorage;
    private UserService userService;
    private AuthService authService;
    private AuthDataAccess authDA;
    private GameService gameService;

    @BeforeEach
    void setUp() throws AlreadyTakenException {
        UserDataAccess userDA = new UserDataAccess();
        AuthDataAccess authDA = new AuthDataAccess();
        GameDataAccess gameDA = new GameDataAccess();
        AuthService authService = new AuthService(authDA);
        GameService gameService = new GameService(gameDA, authService);
        UserService userService = new UserService(userDA, authService);
        this.userService = userService;
        this.gameService = gameService;


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

    @Test
    void clearDb(){
        gameService.clearRequest();
    }
}
