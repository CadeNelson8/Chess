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


        Server server = new Server();
        server.run(port);

        System.out.println("♕ 240 Chess Server running on port " + port);
    }
}
