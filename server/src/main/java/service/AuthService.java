package service;

import dataaccess.AuthDataAccess;
import dataaccess.sql.sqlAuthDataAccess;
import model.AuthData;
import model.UserData;

public class AuthService {

    private final AuthDataAccess authStorage;
    private final sqlAuthDataAccess sqlAuthStorage;

    public AuthService(AuthDataAccess authDA){
        this.authStorage = authDA;
        this.sqlAuthStorage = null;
    }
    public AuthService(sqlAuthDataAccess sqlAuthStorage){
        this.sqlAuthStorage = sqlAuthStorage;
        this.authStorage = null;
    }

    public AuthData createAuth(UserData user){
        AuthData authData = new AuthData(user.username(), AuthData.generateToken());
        sqlAuthStorage.addAuth(authData);
        //authStorage.addAuth(authData);
        return authData;
    }

    public AuthDataAccess getAuthStorage(){
        return authStorage;
    }

    public void clearRequest(){
        authStorage.clear();
    }
}
