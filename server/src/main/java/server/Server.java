package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import model.GameName;
import org.jetbrains.annotations.NotNull;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final UserService userService;
    private final Javalin javalin;
    private final GameService gameService;
    UserDataAccess userDA = new UserDataAccess();
    AuthDataAccess authDA = new AuthDataAccess();
    GameDataAccess gameDA = new GameDataAccess();
    AuthService authService = new AuthService(authDA);


    public Server() {
        GameService gameService = new GameService(gameDA, authService);
        UserService userService = new UserService(userDA, authService);
        this.userService = userService;
        this.gameService = gameService;

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
            javalin.delete("/db", this::clearDb);
            javalin.post("/user", this::register);
            javalin.post("/session",this::login);
            javalin.delete("/session",this::logout);
            javalin.get("/game",this::listGames);
            javalin.post("/game",this::createGame);
            javalin.put("/game",this::joinGame);


        // Register your endpoints and exception handlers here.

    }

    private void clearDb(@NotNull Context ctx){
        userService.clearRequest();
        gameService.clearRequest();
        ctx.status(200);
        ctx.result();
    }

    public void joinGame(@NotNull Context ctx){
        String authToken = ctx.header("authorization");
        if (!authDA.authTaken(authToken)){
            ctx.status(400);
            ctx.result("{\"message\": \"Error: bad request\"}");
            return;
        }
        var gameRequest = new Gson().fromJson(ctx.body(), GameRequest.class);
        try {
            gameService.joinGameRequest(gameRequest, authToken);
            ctx.status(200);
            ctx.result();
        }
        catch(UnauthorizedException e){
            ctx.status(401);
            ctx.result("{\"message\": \"Error: unauthorized\"}");
        }
        catch(AlreadyTakenException a){
            ctx.status(403);
            ctx.result("{\"message\": \"Error: already taken\"}");
        }
    }

    public void createGame(@NotNull Context ctx){
        String auth = ctx.header("authorization");
        if (auth == null){
            ctx.status(400);
            ctx.result("{\"message\": \"Error: bad request\"}");
            return;
        }
        var gameName = new Gson().fromJson(ctx.body(), GameName.class);
        try {
            int id = gameService.createGameRequest(gameName, auth);
            Map<String, Integer> gameID = new HashMap<>();
            gameID.put("gameID", id);
            ctx.status(200);
            ctx.result(new Gson().toJson(gameID));
        }
        catch(UnauthorizedException e){
            ctx.status(401);
            ctx.result("{\"message\": \"Error: unauthorized\"}");
        }
    }

    public void listGames(@NotNull Context ctx){
        String auth = ctx.header("authorization");
        try {
            Map<GameName, GameData> games = gameService.listGames(auth);
            ArrayList<GameData> list = new ArrayList<>();
            for (Map.Entry<GameName, GameData> entry : games.entrySet()){
                list.add(entry.getValue());
            }
            Map<String, ArrayList<GameData>> gameResult = new HashMap<>();
            gameResult.put("games", list);
            ctx.status(200);
            ctx.result(new Gson().toJson(gameResult));
        }
        catch(UnauthorizedException e){
            ctx.status(401);
            ctx.result("{\"message\": \"Error: unauthorized\"}");
        }
    }

    public void register(@NotNull Context ctx){
        var user = new Gson().fromJson(ctx.body(), UserData.class);
        if (user.username()==null || user.password() == null || user.email() == null){
            ctx.status(400);
            ctx.result("{\"message\": \"Error: bad request\"}");
            return;
        }
        try{
            AuthData authData = userService.registerRequest(user);
            ctx.status(200);
            ctx.result(new Gson().toJson(authData));
        }
            catch(AlreadyTakenException e){
            ctx.status(403);
            ctx.result("{\"message\": \"Error: already taken\"}");
        }

    }

    public void login(@NotNull Context ctx){
        var user = new Gson().fromJson(ctx.body(), UserData.class);
        if (user.username()==null || user.password() == null){
            ctx.status(400);
            ctx.result("{\"message\": \"Error: bad request\"}");
            return;
        }
        try {
            AuthData authData = userService.loginRequest(user);
            ctx.status(200);
            ctx.result(new Gson().toJson(authData));
        }
        catch(UnauthorizedException e){
            ctx.status(401);
            ctx.result("{\"message\": \"Error: unauthorized\"}");
        }
    }

    public void logout(@NotNull Context ctx){
        String auth = ctx.header("authorization");
        if (auth == null){
            ctx.status(400);
            ctx.result("{\"message\": \"Error: bad request\"}");
            return;
        }
        try {
            userService.logoutRequest(auth);
            ctx.status(200);
            ctx.result();
        }
        catch(UnauthorizedException e){
            ctx.status(401);
            ctx.result("{\"message\": \"Error: unauthorized\"}");
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
