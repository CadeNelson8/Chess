package server;

import chess.*;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;

import java.util.Scanner;


public class ServerMain {
    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);
    }
}
