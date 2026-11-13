package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.ListGameResult;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;

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

    public ListGameResult executeRequestList(Object user, String method, String path) throws ResponseException{
        var request = HttpRequest.newBuilder()
                .uri(URI.create(URL + path))
                .method(method, makeRequestBody(user));
        request.setHeader("Content-Type", "application/json");
        if(authData != null){
            request.setHeader("authorization", authData.authToken());
        }
        HttpRequest built_request = request.build();
        HttpResponse<String> result = null;
        try {
            result =  client.send(built_request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException (ex.getMessage());
        }
        int status = result.statusCode();
        if (!isSuccessful(status)) {
            var body = result.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }
            throw new ResponseException("other failure: " + status);
        }
        return new Gson().fromJson(result.body(), ListGameResult.class);

    }

    public void executeRequest(Object user, String method, String path) throws ResponseException{
        var request = HttpRequest.newBuilder()
                .uri(URI.create(URL + path))
                .method(method, makeRequestBody(user));
        request.setHeader("Content-Type", "application/json");
        if(authData != null){
            request.setHeader("authorization", authData.authToken());
        }
        HttpRequest built_request = request.build();
        HttpResponse<String> result = null;
        try {
            result =  client.send(built_request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException (ex.getMessage());
        }
        int status = result.statusCode();
        if (!isSuccessful(status)) {
            var body = result.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }
            throw new ResponseException("other failure: " + status);
        }
        authData = new Gson().fromJson(result.body(), AuthData.class);
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }


}
