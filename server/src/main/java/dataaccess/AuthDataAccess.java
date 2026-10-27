package dataaccess;

import model.AuthData;

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

    public void clear(){
        map.clear();
    }
}
