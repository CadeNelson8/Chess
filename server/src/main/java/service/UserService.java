package service;

import dataaccess.AlreadyTakenException;
import dataaccess.UnauthorizedException;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDataAccess userStorage;
    private final AuthService authService;

    public UserService(UserDataAccess userDA, AuthService authService){
        this.userStorage = userDA;
        this.authService = authService;
    }

    public void clearRequest(){
        userStorage.clear();
        authService.clearRequest();
    }

    public AuthData registerRequest(UserData user) throws AlreadyTakenException{
        if(userStorage.userTaken(user.username())){
            throw new AlreadyTakenException("Error: User already taken");
        }
        userStorage.addUser(user);
        AuthData var = authService.createAuth(user);
        return var;
    }

    public AuthData loginRequest(UserData user) throws UnauthorizedException {
        if(!userStorage.userTaken(user.username()) || !userStorage.passwordTaken(user.password())){
            throw new UnauthorizedException("Error: unauthorized");
        }
        UserData real_user = userStorage.getUser(user.username());
        AuthData var = authService.createAuth(real_user);
        return var;
    }

    public void logoutRequest(AuthData auth) throws UnauthorizedException{
        if(!userStorage.userTaken(auth.username()) || !authService.getAuthStorage().authTaken(auth)){
            throw new UnauthorizedException("Error: unauthorized");
        }
        authService.getAuthStorage().deleteAuth(auth);
    }

}
