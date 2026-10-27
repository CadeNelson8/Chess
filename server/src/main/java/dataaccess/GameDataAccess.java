package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameName;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameDataAccess {
    static Map<GameName, GameData> map = new HashMap<>();

    public Map<GameName, GameData> getGames(){
        return map;
    }

    public void add(GameData game){
        this.map.put(game.gameName(), game);
    }

    public static GameData enterGame(int id){
        Iterator<Map.Entry<GameName, GameData>> iterator = map.entrySet().iterator();
        ChessGame g = new ChessGame();
        GameData game = null;
        while (iterator.hasNext()) {
            Map.Entry<GameName, GameData> entry = iterator.next();
            if (entry.getValue().getID()==id) {
                game = entry.getValue();
            }
        }
        return game;
    }

    public void clear(){
        map.clear();
    }
}
