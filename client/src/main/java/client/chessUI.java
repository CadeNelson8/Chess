package client;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.ListGameResult;
import model.UserData;
import server.Color;
import server.GameRequest;

import java.util.Locale;
import java.util.Scanner;

public class chessUI {

    private final ServerFacade facade;
    private boolean logged_in = false;

    public chessUI(String URL){
        facade = new ServerFacade(URL);
    }

    public void drawWhiteBoard(){

    }

    public void printGameList(ListGameResult entry){
        for(GameData game : entry.games()){
            System.out.println("Name: "+game.gameName()
                    +" White username: "+game.whiteUsername()
                    +" Black username: "+game.blackUsername());
        }
    }

    public void logged_inProtocol(String[] inputs){
        if (inputs[0].equalsIgnoreCase("help") && inputs.length == 1){
            System.out.println("Commands: \n"
                    +"create NAME - to start a new game\n"
                    +"list - see all games\n"
                    +"join ID WHITE/BLACK - join a game\n"
                    +"observe ID - observe a game\n"
                    +"logout - leave a game\n"
                    +"quit - leave the chess server\n"
                    +"help - display commands");
        }
        else if (inputs[0].equalsIgnoreCase("create") && inputs.length == 2){
            try {
                GameData user = new GameData(GameData.generateID(),null, null, inputs[1], null);
                facade.executeRequestCreate(user, "post", "/game");
                System.out.println(user.gameName()+" created");
            }
            catch (ResponseException e){
                System.out.println(e.getMessage());
            }
        }
        else if (inputs[0].equalsIgnoreCase("list") && inputs.length == 1){
            try {
                ListGameResult games = facade.executeRequestList(null, "get", "/game");
                printGameList(games);
            } catch (ResponseException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (inputs[0].equalsIgnoreCase("join") && inputs.length == 3){
            try {
                GameRequest request = new GameRequest(null, 0);
                if(inputs[2].equalsIgnoreCase("black")){
                     request = new GameRequest(Color.BLACK, Integer.parseInt(inputs[1]));
                }
                else if(inputs[2].equalsIgnoreCase("white")){
                     request = new GameRequest(Color.WHITE, Integer.parseInt(inputs[1]));
                }
                facade.executeRequestJoin(request, "put", "/game");
                System.out.println(" created");
            }
            catch (ResponseException e){
                System.out.println(e.getMessage());
            }
        }
        else if (inputs[0].equalsIgnoreCase("observe") && inputs.length == 3){
            //facade.executeRequest(inputs);
            drawWhiteBoard();
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
        if (inputs[0].equalsIgnoreCase("help") && inputs.length == 1){
            System.out.println("Commands: \n"
                    +"register USERNAME PASSWORD EMAIL - to create an account\n"
                    +"login USERNAME PASSWORD - to play a game\n"
                    +"quit - to stop playing\n"
                    +"help - display commands");
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
            if (data[0].equalsIgnoreCase("quit") && data.length == 1){
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
