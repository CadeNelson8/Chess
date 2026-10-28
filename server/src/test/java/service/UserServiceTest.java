package service;

import dataaccess.*;
import io.javalin.Javalin;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserDataAccess userStorage;
    private UserService userService;
    private AuthService authService;
    private AuthDataAccess authDA;
    private GameService gameService;

    @BeforeEach
    void setUp(){
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

}