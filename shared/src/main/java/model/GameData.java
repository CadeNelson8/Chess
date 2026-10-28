package model;
import chess.ChessGame;


public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public static int startID = 0;
    public static int generateID(){
        startID++;
        return startID;
    }

    public int getID(){
        return gameID;
    }
}
