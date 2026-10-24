package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class AuthDataAccess {
    Map<String, AuthData> map = new HashMap<>();

    public void addAuth(AuthData authData){
        this.map.put(authData.username(), authData);
    }

    public void deleteAuth(AuthData authData){
        this.map.remove(authData.username());
    }

    public boolean authTaken(AuthData authToken){
        return map.containsValue(authToken);
    }
}
