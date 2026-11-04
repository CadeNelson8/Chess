package service;

import dataaccess.AuthDataAccess;
import dataaccess.sql.SqlAuthDataAccess;
import model.AuthData;
import model.UserData;

public class AuthService {

    private final AuthDataAccess authStorage;
    private final SqlAuthDataAccess sqlAuthStorage;

    public AuthService(AuthDataAccess authDA){
        this.authStorage = authDA;
        this.sqlAuthStorage = null;
    }
    public AuthService(SqlAuthDataAccess sqlAuthStorage){
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
    public SqlAuthDataAccess getSQLAuthStorage(){
        return sqlAuthStorage;
    }

    public void clearRequest(){
        authStorage.clear();
    }

    public void sqlClearRequest(){
        sqlAuthStorage.clear();
    }
}
