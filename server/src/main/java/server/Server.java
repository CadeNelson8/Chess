package server;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import org.jetbrains.annotations.NotNull;
import model.UserData;
import service.UserService;

import java.util.Map;

public class Server {

    private final UserService userService;
    private final Javalin javalin;

    public Server(UserService userService) {
        this.userService = userService;

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
                javalin.delete("/db", Server::clearDb);
        //javalin = Javalin.create(config -> config.staticFiles.add("web"));
           // javalin.delete("/user", Server::register);

        // Register your endpoints and exception handlers here.

    }

    private static void clearDb(@NotNull Context ctx){
        var r = Map.of();
        var json = new Gson();
        ctx.json(json.toJson(r));
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
        if (user.username()==null || user.password() == null || user.email() == null){
            ctx.status(400);
            ctx.result("{\"message\": \"Error: bad request\"}");
            return;
        }
        //AuthData authData = userService.loginRequest(user);
    }


    //

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
