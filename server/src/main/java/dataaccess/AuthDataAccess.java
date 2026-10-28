package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class AuthDataAccess {
    Map<String, AuthData> map = new HashMap<>();

    public void addAuth(AuthData authData){
        this.map.put(authData.authToken(), authData);
    }

    public void deleteAuth(String authData){
        this.map.remove(authData);
    }

    public boolean authTaken(String authToken){
        return map.containsKey(authToken);
    }

    public String getUsername(String authToken){
        for (Map.Entry<String, AuthData> entry : map.entrySet()){
            if(entry.getValue().authToken().equals(authToken)){
                return entry.getValue().username();
            }
        }
        return null;
    }

    public void clear(){
        map.clear();
    }
}
