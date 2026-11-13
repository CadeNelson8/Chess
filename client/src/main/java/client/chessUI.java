package client;

import model.AuthData;
import model.UserData;

import java.util.Scanner;

public class chessUI {

    private final ServerFacade facade;

    public chessUI(String URL){
        facade = new ServerFacade(URL);
    }

    public void logged_inProtocol(String[] inputs){
        if (inputs[0].equalsIgnoreCase("help")){
            System.out.println("Commands: ");
            System.out.println("create NAME - to start a new game");
            System.out.println("list - see all games");
            System.out.println("join ID WHITE/BLACK - join a game");
            System.out.println("logout - leave a game");
            System.out.println("quit - leave the chess server");
            System.out.println("help - display commands");
            System.out.println();
        }
        else if (inputs[0].equalsIgnoreCase("create")){
            if (inputs.length >= 2){
                //facade.executeRequest(inputs);
            }
            else{
                System.out.print("Expected: USERNAME PASSWORD EMAIL");
            }
        }
        else if (inputs[0].equalsIgnoreCase("list")){
            //facade.executeRequest(inputs);
        }
        else if (inputs[0].equalsIgnoreCase("join")){
            if (inputs.length >= 3){
                //facade.executeRequest(inputs);
            }
            else{
                System.out.print("Expected: ID WHITE/BLACK");
            }
        }
        else if (inputs[0].equalsIgnoreCase("logout")){
            AuthData user = new AuthData(inputs[1], null);
            facade.executeRequest(user, "delete", "/session");
            System.out.print("Logout successful");
        }
        else{
            System.out.println("Please enter a valid input");
        }
    }

    public void run() throws ResponseException{
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
            if (data[0].equalsIgnoreCase("quit")){
                break;
            }
            if(logged_in) {
                logged_inProtocol(data);
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
                else if (data[0].equalsIgnoreCase("register")){
                    if (data.length >= 4){
                        try {
                            UserData user = new UserData(data[1], data[2], data[3]);
                            facade.executeRequest(user, "post", "/user");
                            System.out.print("Welcome to chess, "+user.username()+"!");
                        }
                        catch (ResponseException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    else{
                        System.out.print("Expected: USERNAME PASSWORD EMAIL");
                    }
                }
                else if (data[0].equalsIgnoreCase("login")){
                    if (data.length >= 3){
                        try {
                            UserData user = new UserData(data[1], data[2], null);
                            facade.executeRequest(user, "post", "/session");
                            System.out.print("Enjoy your game, "+user.username()+"!");
                            logged_in = true;
                        }
                        catch (ResponseException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    else{
                        System.out.print("Expected: USERNAME PASSWORD");
                    }
                }
                else{
                    System.out.println("Please enter a valid input");
                }
            }
        }
    }

}
