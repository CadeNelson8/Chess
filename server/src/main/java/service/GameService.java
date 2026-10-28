package service;

import chess.ChessGame;
import dataaccess.AlreadyTakenException;
import dataaccess.GameDataAccess;
import dataaccess.UnauthorizedException;
import model.GameData;
import server.Color;
import server.GameRequest;

import java.util.Collection;

public class GameService {
    private final GameDataAccess gameStorage;
    private final AuthService authService;

    public GameService(GameDataAccess gameDA, AuthService authService){
        this.gameStorage = gameDA;
        this.authService = authService;
    }

    public Collection<GameData> listGames(String auth) throws UnauthorizedException{
        if(authService.getAuthStorage().authTaken(auth)){
            return gameStorage.getGames();
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public void clearRequest(){
        gameStorage.clear();
    }

    public int createGameRequest(String gameName, String auth) throws UnauthorizedException{
        if(!authService.getAuthStorage().authTaken(auth)){
            throw new UnauthorizedException("Error: unauthorized");
        }
        ChessGame g = new ChessGame();
        GameData gameData = new GameData(GameData.generateID(), null, null, gameName, g);
        gameStorage.add(gameData);
        return gameData.getID();
    }

    public void joinGameRequest(GameRequest gameRequest, String auth) throws UnauthorizedException, AlreadyTakenException {
        GameData game = GameDataAccess.enterGame(gameRequest.gameID());
        if(game.whiteUsername()==null && gameRequest.playerColor() == Color.WHITE){
            String username = authService.getAuthStorage().getUsername(auth);
            GameData new_game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            gameStorage.add(new_game);
        }
        else if(game.blackUsername()==null && gameRequest.playerColor() == Color.BLACK){
            String username = authService.getAuthStorage().getUsername(auth);
            GameData new_game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            gameStorage.add(new_game);
        }
        else{
            throw new AlreadyTakenException("Error: already taken");
        }

    }

}
