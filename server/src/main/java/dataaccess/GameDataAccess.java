package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameDataAccess {
    static Map<String, GameData> map = new HashMap<>();

    public Map<String, GameData> getGames(){
        return map;
    }

    public void add(GameData game){
        this.map.put(game.gameName(), game);
    }

    public static GameData enterGame(int id){
        Iterator<Map.Entry<String, GameData>> iterator = map.entrySet().iterator();
        ChessGame g = new ChessGame();
        GameData game = null;
        while (iterator.hasNext()) {
            Map.Entry<String, GameData> entry = iterator.next();
            if (entry.getValue().gameID()==id) {
                game = entry.getValue();
            }
        }
        return game;
    }

    public void clear(){
        map.clear();
    }
}
