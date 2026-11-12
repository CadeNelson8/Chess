package client;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {

    private String URL = "http://localhost:";
    private final HttpClient client = HttpClient.newHttpClient();
    private AuthData authData;
    public ServerFacade(String url){
        URL = url;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    String registerRequest(String[] data) throws ResponseException{
        UserData user = new UserData(data[1], data[2], data[3]);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/user"))
                .method("post", makeRequestBody(user));
        request.setHeader("Content-Type", "application/json");
        HttpRequest built_request = request.build();
        HttpResponse<String> result = null;
        try {
            result =  client.send(built_request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            System.out.println("could not process your request");
            return "registration failed";
        }
        var status = result.statusCode();
        if (!isSuccessful(status)) {
            var body = result.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }
            throw new ResponseException("other failure: " + status);
        }
        authData = new Gson().fromJson(result.body(), AuthData.class);

        return "Registration successful. Welcome to chess, " + authData.username() + "!";
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
