package model;
import chess.ChessGame;


public record GameData(String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public static int ID = 0;
    public static int generateID(){
        ID++;
        return ID;
    }

    public int getID(){
        return ID;
    }
}
