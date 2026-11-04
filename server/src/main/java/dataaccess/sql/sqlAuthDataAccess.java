package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.AuthData;

import java.sql.SQLException;

public class sqlAuthDataAccess {

    public void addAuth(AuthData authData)throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth (username, authToken) VALUES(?, ?)")) {
                preparedStatement.setString(1, authData.username());
                preparedStatement.setString(2, authData.authToken());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public void deleteAuth(String auth) throws UnauthorizedException{
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE authToken = ?")) {
                preparedStatement.setString(1, auth);
                int ret = preparedStatement.executeUpdate();
                if (ret == 0) {
                    throw new UnauthorizedException("Error: no such authToken exists");
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public boolean authTaken(String authT) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT authToken FROM auth WHERE authToken = ?")) {
                preparedStatement.setString(1, authT);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var authToken = rs.getString("authToken");
                        if (authToken.equals(authT)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public String getUsername(String authToken){
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM auth WHERE authToken = ?")) {
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery()) {
                    var username = "";
                    while (rs.next()) {
                        username = rs.getString("username");
                    }
                    return username;
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public void clear(){
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auth")) {
                preparedStatement.execute();
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }
}