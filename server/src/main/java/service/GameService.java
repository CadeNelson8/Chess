package service;

import chess.ChessGame;
import dataaccess.AlreadyTakenException;
import dataaccess.GameDataAccess;
import dataaccess.UnauthorizedException;
import dataaccess.sql.sqlGameDataAccess;
import model.GameData;
import server.Color;
import server.GameRequest;

import java.util.Collection;

public class GameService {
    private final GameDataAccess gameStorage;
    private final AuthService authService;
    private final sqlGameDataAccess sqlGameStorage;

    public GameService(GameDataAccess gameDA, AuthService authService){
        this.gameStorage = gameDA;
        this.authService = authService;
        this.sqlGameStorage = null;
    }

    public GameService(sqlGameDataAccess sqlGameDA, AuthService authService){
        this.gameStorage = null;
        this.authService = authService;
        this.sqlGameStorage = sqlGameDA;
    }

    public Collection<GameData> listGames(String auth) throws UnauthorizedException{
        if(authService.getSQLAuthStorage().authTaken(auth)){
            return sqlGameStorage.getGames();
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public void clearRequest(){
        sqlGameStorage.clear();
    }

    public int createGameRequest(String gameName, String auth) throws UnauthorizedException{
        if(!authService.getSQLAuthStorage().authTaken(auth)){
            throw new UnauthorizedException("Error: unauthorized");
        }
        ChessGame g = new ChessGame();
        GameData gameData = new GameData(GameData.generateID(), null, null, gameName, g);
        sqlGameStorage.add(gameData);
        return gameData.getID();
    }

    public void joinGameRequest(GameRequest gameRequest, String auth) throws UnauthorizedException, AlreadyTakenException {
        GameData game = sqlGameStorage.enterGame(gameRequest.gameID());
        if(game.whiteUsername()==null && gameRequest.playerColor() == Color.WHITE){
            String username = authService.getSQLAuthStorage().getUsername(auth);
            GameData new_game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            sqlGameStorage.update(new_game);
        }
        else if(game.blackUsername()==null && gameRequest.playerColor() == Color.BLACK){
            String username = authService.getSQLAuthStorage().getUsername(auth);
            GameData new_game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            sqlGameStorage.update(new_game);
        }
        else{
            throw new AlreadyTakenException("Error: already taken");
        }

    }

}
