package service;

import chess.ChessGame;
import dataaccess.GameDataAccess;
import dataaccess.UnauthorizedException;
import model.AuthData;
import model.GameData;

import java.util.Map;

public class GameService {
    private final GameDataAccess gameStorage;
    private final AuthService authService;

    public GameService(GameDataAccess gameDA, AuthService authService){
        this.gameStorage = gameDA;
        this.authService = authService;
    }

    public Map<String, GameData> listGames(AuthData auth) throws UnauthorizedException{
        if(authService.getAuthStorage().authTaken(auth)){
            return gameStorage.getGames();
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public void clearRequest(){
        gameStorage.clear();
    }

    public int createGameRequest(String gameName, AuthData auth) throws UnauthorizedException{
        if(!authService.getAuthStorage().authTaken(auth)){
            throw new UnauthorizedException("Error: unauthorized");
        }
        ChessGame g = new ChessGame();
        GameData gameData = new GameData(1, "p1", "p2", gameName=gameName, g);
        gameStorage.add(gameData);
        return 1;
    }

    public void joinGameRequest(int id, String color, AuthData auth) throws UnauthorizedException{
        if(!authService.getAuthStorage().authTaken(auth)){
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData game = GameDataAccess.enterGame(id);

    }

}
