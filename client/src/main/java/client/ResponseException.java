package client;

import com.google.gson.Gson;

public class ResponseException extends RuntimeException {

    public String message;

    public ResponseException(String message) {
        super(message);
    }

    public static ResponseException fromJson(String message){
        return new Gson().fromJson(message, ResponseException.class);
    }


}
