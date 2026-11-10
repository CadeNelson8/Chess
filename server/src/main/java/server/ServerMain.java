package server;

import chess.*;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import service.AuthService;
import service.GameService;
import service.UserService;

import java.util.Scanner;


public class ServerMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean logged_in = false;
        while (true) {
            System.out.print("Welcome to 240 Chess. Type Help to get started");
            if(logged_in) {
                System.out.printf("%n[LOGGED_IN] >>> ");
            }
            else{
                System.out.printf("%n[LOGGED_OUT] >>> ");
            }
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine();

            if (line.equalsIgnoreCase("exit")) {
                break;
            }
            if (line.equalsIgnoreCase("help")){
                System.out.println("you have been helped");
            }
            if(logged_in) {
                System.out.printf("%n[LOGGED_IN] >>> ");
            }
            else{
                System.out.printf("%n[LOGGED_OUT] >>> ");
            }
            var numbers = line.split(" ");
            int result = 0;
            try {
                /*for (var number : numbers) {
                    result += Integer.parseInt(number);
                }
                var equation = String.join(" + ", numbers);*
                System.out.printf("%s = [31m%d[0m%n", equation, result);*/
            } catch (NumberFormatException e) {
                System.err.println("Error: Please provide only integers.");
            }
        }
    }
}
