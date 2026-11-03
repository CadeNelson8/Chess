package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class sqlGameDataAccess {
    public Collection<GameData> getGames(){
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM game")) {
                try (var rs = preparedStatement.executeQuery()) {
                    Collection<GameData> games = new ArrayList<>();
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var game = rs.getString("game");
                        System.out.print(game);
                        ChessGame g = new ChessGame();
                        var new_game = new GameData(id, whiteUsername, blackUsername, gameName, g);
                        //turn text into ChessGame
                        games.add(new_game);
                    }
                    return games;
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public void add(GameData game){
        try (var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("INSERT INTO game (id, whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, game.gameID());
                preparedStatement.setString(2, game.whiteUsername());
                preparedStatement.setString(3, game.blackUsername());
                preparedStatement.setString(4, game.gameName());
                preparedStatement.setString(5, game.game().toString());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public GameData enterGame(int id){
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM game WHERE id = ?")) {
                preparedStatement.setInt(1, id);
                try (var rs = preparedStatement.executeQuery()) {
                    GameData new_game = null;
                    while (rs.next()) {
                        int new_id = rs.getInt("id");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var game = rs.getString("game");
                        System.out.print(game);
                        ChessGame g = new ChessGame();
                        new_game = new GameData(new_id, whiteUsername, blackUsername, gameName, g);
                    }
                    return new_game;
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public void clear(){
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE game")) {
                preparedStatement.execute();
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }
}
