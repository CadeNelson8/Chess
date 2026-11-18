package client;


import model.*;


import java.util.Scanner;

import static client.WhiteBoard.blackBoard;
import static client.WhiteBoard.whiteBoard;

public class ChessUI {

    private final ServerFacade facade;
    private static boolean loggedIn = false;
    private ListGameResult games;

    public ChessUI(String url){
        facade = new ServerFacade(url);
    }



    public void printGameList(ListGameResult entry){
        int i = 1;
        for(GameData game : entry.games()){
            System.out.println(i+") Name: "+game.gameName()
                    +" White username: "+game.whiteUsername()
                    +" Black username: "+game.blackUsername());
            i++;
        }
    }

    public boolean verifyList(String[] inputs){
        if(games==null){
            System.out.println("Please call list before joining a game");
            return false;
        }
        int gameIndex = -1;
        try {
            gameIndex = Integer.parseInt(inputs[1]) - 1;
        }
        catch(NumberFormatException e){
            System.out.println("Please enter a valid ID");
            return false;
        }
        if(0 > gameIndex || games.games().size() <= gameIndex){
            System.out.println("Please enter a valid game ID");
            return false;
        }
        return true;
    }

    public void loggedInProtocol(String[] inputs){
        if (inputs[0].equalsIgnoreCase("help") && inputs.length == 1){
            System.out.println("Commands: \n" +"create NAME - to start a new game\n"
                    +"list - see all games\n" +"join ID WHITE/BLACK - join a game\n"
                    +"observe ID - observe a game\n" +"logout - leave a game\n"
                    +"quit - leave the chess server\n"
                    +"help - display commands");
        }
        else if (inputs[0].equalsIgnoreCase("create") && inputs.length == 2){
            try {
                GameData user = new GameData(GameData.generateID(),null, null, inputs[1], null);
                facade.executeRequest(user, "post", "/game", false);
                System.out.println(user.gameName()+" created");
            }
            catch (ResponseException e){
                System.out.println(e.getMessage());
            }
        }
        else if (inputs[0].equalsIgnoreCase("list") && inputs.length == 1){
            try {
                games = facade.executeRequest(null, "get", "/game", false);
                printGameList(games);
            } catch (ResponseException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (inputs[0].equalsIgnoreCase("join") && inputs.length == 3){
            try {
                GameRequest request = new GameRequest(null, 0);
                if(verifyList(inputs)==false){
                    return;
                }
                int gameIndex = -1;
                gameIndex = Integer.parseInt(inputs[1]) - 1;
                GameData progress = games.games().get(gameIndex);
                if(inputs[2].equalsIgnoreCase("black")){
                     request = new GameRequest(Color.BLACK, progress.gameID());
                }
                else if(inputs[2].equalsIgnoreCase("white")){
                     request = new GameRequest(Color.WHITE, progress.gameID());
                }
                facade.executeRequest(request, "put", "/game", false);
                if(inputs[2].equalsIgnoreCase("black")){
                    blackBoard();
                }
                else if(inputs[2].equalsIgnoreCase("white")){
                    whiteBoard();
                }
                System.out.println("game joined");
            }
            catch (ResponseException e){
                System.out.println(e.getMessage());
            }
        }
        else if (inputs[0].equalsIgnoreCase("observe") && inputs.length == 2){
            if(verifyList(inputs)==false){
                return;
            }
            whiteBoard();
        }
        else if (inputs[0].equalsIgnoreCase("logout") && inputs.length == 1){
            try {
                AuthData user = new AuthData(null, null);
                facade.executeRequest(user, "delete", "/session", true);
                System.out.println("Logout successful");
                loggedIn = false;
            } catch (ResponseException e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("Please enter a valid input");
        }
    }

    public void loggedOutProtocol(String[] inputs){
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
                facade.executeRequest(user, "post", "/user", true);
                System.out.println("Welcome to chess, "+user.username()+"!");
            }
            catch (ResponseException e){
                System.out.println(e.getMessage());
            }
        }
        else if (inputs[0].equalsIgnoreCase("login") && inputs.length == 3){
            try {
                UserData user = new UserData(inputs[1], inputs[2], null);
                facade.executeRequest(user, "post", "/session", true);
                System.out.println("Enjoy your game, "+user.username()+"!");
                loggedIn = true;
            }
            catch (ResponseException e){
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("Please enter a valid input");
        }
    }

    public void run() throws ResponseException{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to 240 Chess. Type Help to get started");
        System.out.printf("%n[LOGGED_OUT] >>> ");
        while (true) {
            if (!scanner.hasNextLine()){ break;}
            String line = scanner.nextLine();
            var data = line.split(" ");
            if (data[0].equalsIgnoreCase("quit") && data.length == 1){
                break;
            }
            if(loggedIn) {
                loggedInProtocol(data);
                if(loggedIn){
                    System.out.printf("%n[LOGGED_IN] >>> ");
                }
                else{
                    System.out.printf("%n[LOGGED_OUT] >>> ");
                }
            }
            else{
                loggedOutProtocol(data);
                if(loggedIn){
                    System.out.printf("%n[LOGGED_IN] >>> ");
                }
                else{
                    System.out.printf("%n[LOGGED_OUT] >>> ");
                }
            }
        }
    }

}
