package client;

import model.UserData;

import java.util.Scanner;

public class chessUI {

    private final ServerFacade facade;

    public chessUI(String URL){
        facade = new ServerFacade(URL);

    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean logged_in = false;
        System.out.print("Welcome to 240 Chess. Type Help to get started");
        while (true) {
            if(logged_in) {
                System.out.printf("%n[LOGGED_IN] >>> ");
            }
            else{
                System.out.printf("%n[LOGGED_OUT] >>> ");
            }
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine();
            var data = line.split(" ");
            if(logged_in) {
                if (data[0].equalsIgnoreCase("quit")){
                    break;
                }
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
                else if (data[0].equalsIgnoreCase("create")){
                    if (data.length >= 2){
                        var request = facade.registerRequest(data);
                        System.out.print(request);
                    }
                    else{
                        System.out.print("Expected: USERNAME PASSWORD EMAIL");
                    }
                }
                else if (data[0].equalsIgnoreCase("list")){
                    if (data.length >= 1){
                        var request = facade.registerRequest(data);
                        System.out.print(request);
                    }
                    else{
                        System.out.print("Expected: list");
                    }
                }
                else if (data[0].equalsIgnoreCase("join")){
                    if (data.length >= 3){
                        var request = facade.registerRequest(data);
                        System.out.print(request);
                    }
                    else{
                        System.out.print("Expected: ID WHITE/BLACK");
                    }
                }
                else if (data[0].equalsIgnoreCase("logout")){
                    if (data.length >= 1){
                        var request = facade.registerRequest(data);
                        System.out.print(request);
                    }
                    else{
                        System.out.print("Expected: ID WHITE/BLACK");
                    }
                }
                else{
                    System.out.println("Please enter a valid input");
                }
            }
            else{
                if (data[0].equalsIgnoreCase("help")){
                    System.out.println("Commands: ");
                    System.out.println("register USERNAME PASSWORD EMAIL - to create an account");
                    System.out.println("login USERNAME PASSWORD - to play a game");
                    System.out.println("quit - to stop playing");
                    System.out.println("help - display commands");
                    System.out.println();
                }
                if (data[0].equalsIgnoreCase("quit")){
                    break;
                }
                else if (data[0].equalsIgnoreCase("register")){
                    if (data.length >= 4){
                        var request = facade.registerRequest(data);
                        System.out.print(request);
                    }
                    else{
                        System.out.print("Expected: USERNAME PASSWORD EMAIL");
                    }
                }
                /*else if (data[0].equalsIgnoreCase("login")){
                    if (data.length >= 3){
                        //UserData user = server.getUser(data[1]);
                        var request = facade.loginRequest(data);
                        System.out.print(request);
                        logged_in = true;
                    }
                    else{
                        System.out.print("Expected: USERNAME PASSWORD");
                    }
                }*/
                else{
                    System.out.println("Please enter a valid input");
                }
            }
        }
    }
}
