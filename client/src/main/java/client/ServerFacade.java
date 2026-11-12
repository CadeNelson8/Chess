package client;

import com.google.gson.Gson;
import model.UserData;

import java.net.URI;
import java.net.http.HttpRequest;

public class ServerFacade {

    private String URL = "http://localhost:";

    public ServerFacade(String url){

        URL = url;
    }

    HttpRequest registerRequest(String[] data){
        UserData user = new UserData(data[1], data[2], data[3]);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/user"))
                .method("register", makeRequestBody(user));
        if (user != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    /*HttpRequest loginRequest(String[] data){

        var request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/user"))
                .method("register", makeRequestBody(user));
        if (user != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }*/

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }


}
