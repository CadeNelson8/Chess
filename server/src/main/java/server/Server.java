package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import model.UserData;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", Server::clearDb);

        // Register your endpoints and exception handlers here.

    }

    private static void clearDb(@NotNull Context ctx){
        var r = Map.of();
        var json = new Gson();
        ctx.json(json.toJson(r));
    }

    //register method
    public String register(String username, String password, String email){
        var user = new UserData(username, password, email);
        //check if user already taken
        

        return username;
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
