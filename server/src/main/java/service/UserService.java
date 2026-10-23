package service;

import dataaccess.AlreadyTakenException;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDataAccess userDA;
    private final AuthService authService;

    public UserService(UserDataAccess userDA, AuthService authService){
        this.userDA = userDA;
        this.authService = authService;
    }

    public AuthData registerRequest(UserData user) throws AlreadyTakenException{
        if(userDA.userTaken(user.username())){
            throw new AlreadyTakenException("Error: User already taken");
        }
        userDA.addUser(user);
        AuthData var = authService.createAuth(user);
        return var;
    }

    //public AuthData loginRequest(UserData user){


    //}

}
