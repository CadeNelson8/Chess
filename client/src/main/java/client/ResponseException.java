package client;

import com.google.gson.Gson;

import java.util.Map;

public class ResponseException extends RuntimeException {

    public String message;

    public ResponseException(String message) {
        super(message);
    }

    public static ResponseException fromJson(String m){
        var temp = new Gson().fromJson(m, Map.class);
        String mess = temp.get("message").toString();
        ResponseException exception = new ResponseException(mess);
        return exception;
    }


}
