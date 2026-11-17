package client;

import com.google.gson.Gson;
import model.AuthData;
import model.ListGameResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {

    private String url = "http://localhost:";
    private final HttpClient client = HttpClient.newHttpClient();
    private AuthData authData;
    public ServerFacade(String url){
        this.url = url;
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public ListGameResult executeRequestList(Object user, String method, String path, boolean needAuth) throws ResponseException{
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url + path))
                .method(method, makeRequestBody(user));
        request.setHeader("Content-Type", "application/json");
        if(authData.authToken() != null){
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
        if(needAuth) {
            authData = new Gson().fromJson(result.body(), AuthData.class);
        }
        return new Gson().fromJson(result.body(), ListGameResult.class);
    }

    public ListGameResult executeRequest(Object user, String method, String path, boolean needAuth) throws ResponseException{
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url + path))
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
        if(needAuth) {
            authData = new Gson().fromJson(result.body(), AuthData.class);
        }
        return new Gson().fromJson(result.body(), ListGameResult.class);
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }


}
