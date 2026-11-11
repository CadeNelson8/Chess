package client;

import chess.*;

import java.util.Scanner;

public class ClientMain {



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
            if(logged_in) {
                if (line.equalsIgnoreCase("help")){
                    System.out.println("Commands: ");
                    System.out.println("create NAME - to start a new game");
                    System.out.println("list - see all games");
                    System.out.println("join ID WHITE/BLACK - join a game");
                    System.out.println("logout - leave a game");
                    System.out.println("quit - leave the chess server");
                    System.out.println("help - display commands");
                    System.out.println();
                }
            }
            else{
                var data = line.split(" ");
                if (line.equalsIgnoreCase("help")){
                    System.out.println("Commands: ");
                    System.out.println("register USERNAME PASSWORD EMAIL - to create an account");
                    System.out.println("login USERNAME PASSWORD - to play a game");
                    System.out.println("quit - to stop playing");
                    System.out.println("help - display commands");
                    System.out.println();
                }
                else if (line.equalsIgnoreCase("quit")){
                    break;
                }
                else if (data[0].equalsIgnoreCase("register")){

                }
                else if (data[0].equalsIgnoreCase("login")){

                }
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
