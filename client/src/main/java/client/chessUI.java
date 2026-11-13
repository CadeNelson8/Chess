package client;

import model.AuthData;
import model.GameData;
import model.ListGameResult;
import model.UserData;

import java.util.Scanner;

public class chessUI {

    private final ServerFacade facade;
    private boolean logged_in = false;

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
        else if (inputs[0].equalsIgnoreCase("create") && inputs.length == 2){
            try {
                GameData user = new GameData(GameData.generateID(),null, null, inputs[1], null);
                facade.executeRequest(user, "post", "/game");
                System.out.println(user.gameName()+" created");
            }
            catch (ResponseException e){
                System.out.println(e.getMessage());
            }
        }
        else if (inputs[0].equalsIgnoreCase("list") && inputs.length == 1){
            try {
                ListGameResult games = facade.executeRequestList(null, "get", "/game");
                System.out.println(games.games());
            } catch (ResponseException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (inputs[0].equalsIgnoreCase("join") && inputs.length == 3){
            //facade.executeRequest(inputs);
        }
        else if (inputs[0].equalsIgnoreCase("logout") && inputs.length == 2){
            try {
                AuthData user = new AuthData(inputs[1], null);
                facade.executeRequest(user, "delete", "/session");
                System.out.println("Logout successful");
                logged_in = false;
            } catch (ResponseException e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("Please enter a valid input");
        }
        if(logged_in){
            System.out.printf("%n[LOGGED_IN] >>> ");
        }
        else{
            System.out.printf("%n[LOGGED_OUT] >>> ");
        }
    }

    public void logged_outProtocol(String[] inputs){
        if (inputs[0].equalsIgnoreCase("help")){
            System.out.println("Commands: ");
            System.out.println("register USERNAME PASSWORD EMAIL - to create an account");
            System.out.println("login USERNAME PASSWORD - to play a game");
            System.out.println("quit - to stop playing");
            System.out.println("help - display commands");
            System.out.println();
        }
        else if (inputs[0].equalsIgnoreCase("register") && inputs.length == 4){
            try {
                UserData user = new UserData(inputs[1], inputs[2], inputs[3]);
                facade.executeRequest(user, "post", "/user");
                System.out.println("Welcome to chess, "+user.username()+"!");
            }
            catch (ResponseException e){
                System.out.println(e.getMessage());
            }
        }
        else if (inputs[0].equalsIgnoreCase("login") && inputs.length == 3){
            try {
                UserData user = new UserData(inputs[1], inputs[2], null);
                facade.executeRequest(user, "post", "/session");
                System.out.println("Enjoy your game, "+user.username()+"!");
                logged_in = true;
            }
            catch (ResponseException e){
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("Please enter a valid input");
        }
        if(logged_in){
            System.out.printf("%n[LOGGED_IN] >>> ");
        }
        else{
            System.out.printf("%n[LOGGED_OUT] >>> ");
        }
    }

    public void run() throws ResponseException{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to 240 Chess. Type Help to get started");
        System.out.printf("%n[LOGGED_OUT] >>> ");
        while (true) {
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
                logged_outProtocol(data);

            }
        }
    }

}
