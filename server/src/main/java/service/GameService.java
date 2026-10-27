package service;

import chess.ChessGame;
import dataaccess.AlreadyTakenException;
import dataaccess.GameDataAccess;
import dataaccess.UnauthorizedException;
import model.AuthData;
import model.GameData;
import server.GameRequest;
import model.GameName;

import java.util.Map;

public class GameService {
    private final GameDataAccess gameStorage;
    private final AuthService authService;

    public GameService(GameDataAccess gameDA, AuthService authService){
        this.gameStorage = gameDA;
        this.authService = authService;
    }

    public Map<GameName, GameData> listGames(String auth) throws UnauthorizedException{
        if(authService.getAuthStorage().authTaken(auth)){
            return gameStorage.getGames();
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public void clearRequest(){
        gameStorage.clear();
    }

    public int createGameRequest(GameName gameName, String auth) throws UnauthorizedException{
        if(!authService.getAuthStorage().authTaken(auth)){
            throw new UnauthorizedException("Error: unauthorized");
        }
        ChessGame g = new ChessGame();
        GameData gameData = new GameData( null, null, gameName, g);
        gameStorage.add(gameData);
        return gameData.getID();
    }

    public void joinGameRequest(GameRequest gameRequest, String auth) throws UnauthorizedException, AlreadyTakenException {
        if(!authService.getAuthStorage().authTaken(auth)){
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData game = GameDataAccess.enterGame(gameRequest.gameID());
        if(game.blackUsername()==null && game.whiteUsername() == null){
            throw new AlreadyTakenException("Error: already taken");
        }
    }

}
