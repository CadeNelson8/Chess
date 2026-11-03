package dataaccess.sql;

import dataaccess.DataAccessException;
import model.UserData;

import java.sql.SQLException;

public class sqlUserDataAccess {
    public void addUser(UserData user){
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?)")) {
            preparedStatement.setString(1, user.username());
            preparedStatement.setString(2, user.password());
            preparedStatement.setString(2, user.email());
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public UserData getUser(String username){
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("SELECT user FROM user WHERE username = ?")) {
            preparedStatement.setString(1, username);
            try (var rs = preparedStatement.executeQuery()){
                UserData user = null;
                while(rs.next()){
                    var n_username = rs.getString("username");
                    var n_password = rs.getString("password");
                    var n_email = rs.getString("email");
                    user = new UserData(n_username, n_password, n_email);
                }
                return user;
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public boolean userTaken(String username){
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("SELECT user FROM user WHERE username = ?")) {
            preparedStatement.setString(1, username);
            try (var rs = preparedStatement.executeQuery()){
                while(rs.next()){
                    var usern = rs.getString("username");
                    if (usern.equals(username)){
                        return true;
                    }
                }
                return false;
            }
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public boolean passwordCorrect(String username, String password) {
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("SELECT username, password FROM user WHERE username = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var usern = rs.getString("username");
                    var passwordn = rs.getString("password");
                    if (usern.equals(username) && (passwordn.equals(password))) {
                        return true;
                    }
                }
                return false;
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection");
        }
    }

    public void clear(){
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("TRUNCATE TABLE user")) {
            preparedStatement.execute();
        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }
}
