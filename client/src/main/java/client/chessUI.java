package client;


import model.AuthData;
import model.GameData;
import model.ListGameResult;
import model.UserData;
import server.Color;
import server.GameRequest;

import java.util.Scanner;

import static client.WhiteBoard.blackBoard;
import static client.WhiteBoard.whiteBoard;

public class chessUI {

    private final ServerFacade facade;
    private boolean logged_in = false;
    private ListGameResult games;

    public chessUI(String URL){
        facade = new ServerFacade(URL);
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
                games = facade.executeRequestList(null, "get", "/game");
                printGameList(games);
            } catch (ResponseException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (inputs[0].equalsIgnoreCase("join") && inputs.length == 3){
            try {
                GameRequest request = new GameRequest(null, 0);
                if(games==null){
                    System.out.println("Please call list before joining a game");
                    return;
                }
                int gameIndex = -1;
                try {
                    gameIndex = Integer.parseInt(inputs[1]) - 1;
                }
                catch(NumberFormatException e){
                    System.out.println("Please enter a valid ID");
                    return;
                }
                if(0 > gameIndex || games.games().size() < gameIndex){
                    System.out.println("Please enter a valid game ID");
                    return;
                }
                GameData progress = games.games().get(gameIndex);
                if(inputs[2].equalsIgnoreCase("black")){
                     request = new GameRequest(Color.BLACK, progress.gameID());
                }
                else if(inputs[2].equalsIgnoreCase("white")){
                     request = new GameRequest(Color.WHITE, progress.gameID());
                }
                facade.executeRequestJoin(request, "put", "/game");
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
            if(games==null){
                System.out.println("Please call list before joining a game");
                return;
            }
            int gameIndex = -1;
            try {
                gameIndex = Integer.parseInt(inputs[1]) - 1;
            }
            catch(NumberFormatException e){
                System.out.println("Please enter a valid ID");
                return;
            }
            if(0 > gameIndex || games.games().size() < gameIndex){
                System.out.println("Please enter a valid game ID");
                return;
            }
            whiteBoard();
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
                if(logged_in){
                    System.out.printf("%n[LOGGED_IN] >>> ");
                }
                else{
                    System.out.printf("%n[LOGGED_OUT] >>> ");
                }
            }
            else{
                logged_outProtocol(data);
                if(logged_in){
                    System.out.printf("%n[LOGGED_IN] >>> ");
                }
                else{
                    System.out.printf("%n[LOGGED_OUT] >>> ");
                }
            }
        }
    }

}
