package server;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.UnauthorizedException;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import model.UserData;
import service.GameService;
import service.UserService;

import java.util.Map;

public class Server {
    private final UserService userService;
    private final Javalin javalin;
    private final GameService gameService;

    public Server(UserService userService, GameService gameService) {
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
        var auth = new Gson().fromJson(ctx.body(), AuthData.class);
        // need to get player color and id as well
        String color = "";
        int id = 1;
        /*if (user.username()==null || user.password() == null || user.email() == null){
            ctx.status(400);
            ctx.result("{\"message\": \"Error: bad request\"}");
            return;
        }*/
        try {
            gameService.joinGameRequest(id, color, auth);
            ctx.status(200);
            ctx.result(new Gson().toJson(id));
        }
        catch(UnauthorizedException e){
            ctx.status(401);
            ctx.result("{\"message\": \"Error: unauthorized\"}");
        }
    }

    public void createGame(@NotNull Context ctx){
        var auth = new Gson().fromJson(ctx.body(), AuthData.class);
        // need to get game name as well
        String gameName = "";
        /*if (user.username()==null || user.password() == null || user.email() == null){
            ctx.status(400);
            ctx.result("{\"message\": \"Error: bad request\"}");
            return;
        }*/
        try {
            int id = gameService.createGameRequest(gameName, auth);
            ctx.status(200);
            ctx.result(new Gson().toJson(id));
        }
        catch(UnauthorizedException e){
            ctx.status(401);
            ctx.result("{\"message\": \"Error: unauthorized\"}");
        }
    }

    public void listGames(@NotNull Context ctx){
        var auth = new Gson().fromJson(ctx.body(), AuthData.class);
        try {
            Map<String, GameData> games = gameService.listGames(auth);
            ctx.status(200);
            ctx.result(new Gson().toJson(games));
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
        var auth = new Gson().fromJson(ctx.body(), AuthData.class);
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
