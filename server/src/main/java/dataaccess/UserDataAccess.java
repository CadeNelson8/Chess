package dataaccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;

public class UserDataAccess {
    Map<String, UserData> map = new HashMap<>();

    public void addUser(UserData user){
        this.map.put(user.username(), user);
    }

    public UserData getUser(String username){
        return map.get(username);
    }

    public boolean userTaken(String username){
        return map.containsKey(username);
    }

    public boolean passwordCorrect(String username, String password){
        return map.get(username).password().equals(password);
    }

    public void clear(){
        map.clear();
    }

}
