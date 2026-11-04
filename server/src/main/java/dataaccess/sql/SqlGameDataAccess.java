package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SqlGameDataAccess {
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
                        var game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        var new_game = new GameData(id, whiteUsername, blackUsername, gameName, game);
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

    public void update(GameData game){
        try (var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE id=?")) {
                preparedStatement.setInt(5, game.gameID());
                preparedStatement.setString(1, game.whiteUsername());
                preparedStatement.setString(2, game.blackUsername());
                preparedStatement.setString(3, game.gameName());
                var new_game = new Gson().toJson(game);
                preparedStatement.setString(4, new_game);
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            throw new DataAccessException(e+"Update failed to get connection");
        }
    }

    public void add(GameData game){
        try (var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("INSERT INTO game (id, whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, game.gameID());
                preparedStatement.setString(2, game.whiteUsername());
                preparedStatement.setString(3, game.blackUsername());
                preparedStatement.setString(4, game.gameName());
                var new_game = new Gson().toJson(game);
                preparedStatement.setString(5, new_game);
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            throw new DataAccessException(e+"add failed to get connection");
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
                        var game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        new_game = new GameData(id, whiteUsername, blackUsername, gameName, game);
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
