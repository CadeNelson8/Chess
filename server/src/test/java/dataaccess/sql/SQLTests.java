package dataaccess.sql;

import chess.ChessBoard;
import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import io.javalin.Javalin;
import model.AuthData;
import model.GameData;
import model.GameName;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.AuthService;
import service.GameService;
import service.UserService;

import java.sql.SQLException;
import java.util.Collection;

import static dataaccess.sql.DatabaseManager.createDatabase;
import static org.junit.jupiter.api.Assertions.*;

public class SQLTests {
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
    void addGameSuccess(){
        ChessGame game = new ChessGame();
        GameData new_game = new GameData(1, "white", "black", "name", game);
        sqlGame.add(new_game);
        GameData check = sqlGame.enterGame(1);
        assertEquals(check, new_game);
    }

    @Test
    void updateGameSuccess(){
        ChessGame game = new ChessGame();
        GameData new_game = new GameData(1, "white", "black", "name", game);
        sqlGame.add(new_game);
        sqlGame.update(new_game);
        GameData check = sqlGame.enterGame(1);
        assertEquals(check, new_game);
    }

    @Test
    void getGamesSuccess(){
        ChessGame game = new ChessGame();
        GameData new_game = new GameData(1, "white", "black", "name", game);
        sqlGame.add(new_game);
        Collection<GameData> games = sqlGame.getGames();

    }

    @Test
    void authTakenSuccess(){
        AuthData auth = new AuthData("username1", "authToken");
        sqlAuth.addAuth(auth);
        boolean test = sqlAuth.authTaken("authToken");
        assertEquals(true, test);
    }

    @Test
    void authNotTakenSuccess(){
        AuthData auth = new AuthData("username1", "authToken");
        sqlAuth.addAuth(auth);
        boolean test = sqlAuth.authTaken("authn");
        assertEquals(false, test);
    }

    @Test
    void authTakenException(){

        AuthData auth = new AuthData("username1", "authToken");
        sqlAuth.addAuth(auth);
        boolean test = sqlAuth.authTaken("authToken");
        assertEquals(true, test);
    }

    @Test
    void addAuthSuccess(){
        AuthData auth = new AuthData("username1", "authToken");
        sqlAuth.addAuth(auth);

        assertEquals("username1", auth.username());
    }

    @Test
    void deleteAuthSuccess(){
        AuthData auth = new AuthData("username1", "authToken");
        sqlAuth.addAuth(auth);
        sqlAuth.deleteAuth("authToken");
        String user = sqlAuth.getUsername("authToken");
        assertEquals("", user);
    }

    @Test
    void addUserSuccess(){
        UserData user = new UserData("username1", "password1", "me@email");
        sqlUserDA.addUser(user);
        assertEquals(user.email(), "me@email");
    }

    @Test
    void addUserFail(){
        UserData user = new UserData("username1", "password1", "me@email");
        sqlUserDA.addUser(user);
        assertNotEquals(user.email(), "me@l");
    }

    @Test
    void getUserSuccess(){
        UserData user = new UserData("username1", "password1", "me@email");
        sqlUserDA.addUser(user);

        UserData copy = sqlUserDA.getUser("username1");
        assertEquals(user.email(), copy.email());
    }

    @Test
    void userTakenSuccess(){
        UserData user = new UserData("username1", "password1", "me@email");
        sqlUserDA.addUser(user);

        boolean test = sqlUserDA.userTaken("username1");
        assertEquals(test, true);
    }

    @Test
    void userTakenDouble(){
        UserData user = new UserData("username1", "password1", "me@email");
        sqlUserDA.addUser(user);
        UserData user1 = new UserData("username1", "password1", "me@email");

        assertThrows(DataAccessException.class, ()->sqlUserDA.addUser(user1));
    }

    @Test
    void userNotTakenSuccess(){
        UserData user = new UserData("username1", "password1", "me@email");
        sqlUserDA.addUser(user);

        boolean test = sqlUserDA.userTaken("username2");
        assertEquals(test, false);
    }

    @Test
    void passwordCorrectSuccess(){
        UserData user = new UserData("username1", "password1", "me@email");
        sqlUserDA.addUser(user);

        boolean test = sqlUserDA.passwordCorrect("username1", "password1");
        assertEquals(true, test);
    }


}
