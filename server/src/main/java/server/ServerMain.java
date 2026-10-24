package server;

import chess.*;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import service.AuthService;
import service.GameService;
import service.UserService;


public class ServerMain {
    public static void main(String[] args) {
        var port = 8080;
        UserDataAccess userDA = new UserDataAccess();
        AuthDataAccess authDA = new AuthDataAccess();
        GameDataAccess gameDA = new GameDataAccess();
        AuthService authService = new AuthService(authDA);
        GameService gameService = new GameService(gameDA, authService);
        UserService userService = new UserService(userDA, authService);

        Server server = new Server(userService, gameService);
        server.run(port);

        System.out.println("♕ 240 Chess Server running on port " + port);
    }
}
