package dataaccess.sql;

import dataaccess.DataAccessException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SqlUserDataAccess {
    public void addUser(UserData user){
        try (var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, user.username());
                String hashpassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
                preparedStatement.setString(2, hashpassword);
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            throw new DataAccessException(e+"failed to get connection");
        }
    }

    public UserData getUser(String username){
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM user WHERE username = ?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    UserData user = null;
                    while (rs.next()) {
                        var n_username = rs.getString("username");
                        var n_password = rs.getString("password");
                        var n_email = rs.getString("email");
                        user = new UserData(n_username, n_password, n_email);
                    }
                    return user;
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public boolean userTaken(String username){
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM user WHERE username = ?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var usern = rs.getString("username");
                        return usern.equals(username);
                    }
                    return false;
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public boolean passwordCorrect(String username, String password) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, password FROM user WHERE username = ?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var usern = rs.getString("username");
                        var passwordn = rs.getString("password");
                        boolean match = BCrypt.checkpw(password, passwordn);
                        return match && usern.equals(username);
                    }
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection");
        }
    }

    public void clear(){
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE user")) {
                preparedStatement.execute();
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }
}
