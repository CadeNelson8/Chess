package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameDataAccess {
    Map<Integer, GameData> map = new HashMap<>();

    public Collection<GameData> getGames(){
        return map.values();
    }

    public void add(GameData game){
        map.put(game.gameID(), game);
    }

    public GameData enterGame(int id){
        return map.get(id);
    }

    public void clear(){
        map.clear();
    }
}
