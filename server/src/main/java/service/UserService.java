package service;

import dataaccess.AlreadyTakenException;
import dataaccess.UnauthorizedException;
import dataaccess.UserDataAccess;
import dataaccess.sql.sqlUserDataAccess;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private final UserDataAccess userStorage;
    private final AuthService authService;
    private final sqlUserDataAccess sqlUserStorage;


    public UserService(UserDataAccess userDA, AuthService authService){
        this.userStorage = userDA;
        this.authService = authService;
        this.sqlUserStorage = null;
    }

    public UserService(sqlUserDataAccess sqlUserDA, AuthService authService){
        this.userStorage = null;
        this.authService = authService;
        this.sqlUserStorage = sqlUserDA;
    }

    public void clearRequest(){
        sqlUserStorage.clear();
        authService.SQLclearRequest();
    }

    public AuthData registerRequest(UserData user) throws AlreadyTakenException{
        if(sqlUserStorage.userTaken(user.username())){
            throw new AlreadyTakenException("Error: User already taken");
        }
        sqlUserStorage.addUser(user);
        AuthData var = authService.createAuth(user);
        return var;
    }

    public AuthData loginRequest(UserData user) throws UnauthorizedException {
        if(sqlUserStorage.userTaken(user.username()) && sqlUserStorage.passwordCorrect(user.username(), user.password())){
            UserData real_user = sqlUserStorage.getUser(user.username());
            AuthData var = authService.createAuth(real_user);
            return var;
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public void logoutRequest(String auth) throws UnauthorizedException{
        if(!authService.getSQLAuthStorage().authTaken(auth)){
            throw new UnauthorizedException("Error: unauthorized");
        }
        authService.getSQLAuthStorage().deleteAuth(auth);
    }

}
