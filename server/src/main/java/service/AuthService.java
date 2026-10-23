package service;

import dataaccess.AuthDataAccess;
import model.AuthData;
import model.UserData;

public class AuthService {

    private final AuthDataAccess authStorage;

    public AuthService(AuthDataAccess authDA){
        this.authStorage = authDA;
    }

    public AuthData createAuth(UserData user){
        AuthData authData = new AuthData(user.username(), AuthData.generateToken());
        authStorage.addAuth(authData);
        return authData;
    }

}
