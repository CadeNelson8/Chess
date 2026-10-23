package server;

import chess.*;
import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import service.AuthService;
import service.UserService;


public class ServerMain {
    public static void main(String[] args) {
        var port = 8080;
        UserDataAccess userDA = new UserDataAccess();
        AuthDataAccess authDA = new AuthDataAccess();
        AuthService authService = new AuthService(authDA);
        UserService userService = new UserService(userDA, authService);

        Server server = new Server(userService);
        server.run(port);

        System.out.println("♕ 240 Chess Server running on port " + port);
    }
}
