package client;

import model.UserData;

import java.net.URI;
import java.net.http.HttpRequest;

public class ServerFacade {

    private final String URL;

    public ServerFacade(String URL){
        this.URL = URL;
    }

    public void registerRequest(UserData data){
        var request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                ;

    }


}
