package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameName;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameDataAccess {
    static Map<Integer, GameData> map = new HashMap<>();

    public Collection<GameData> getGames(){
        return map.values();
    }

    public void add(GameData game){
        map.put(game.gameID(), game);
    }

    public static GameData enterGame(int id){
        Iterator<Map.Entry<Integer, GameData>> iterator = map.entrySet().iterator();
        GameData game = null;
        while (iterator.hasNext()) {
            Map.Entry<Integer, GameData> entry = iterator.next();
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
