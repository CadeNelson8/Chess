package dataaccess.sql;

import dataaccess.DataAccessException;
import model.AuthData;

import java.sql.SQLException;
import java.sql.Statement;

public class sqlAuthDataAccess {

    public void addAuth(AuthData authData)throws DataAccessException {
            try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("INSERT INTO auth (username, authToken) VALUES(?, ?)")) {
                preparedStatement.setString(1, authData.username());
                preparedStatement.setString(2, authData.authToken());

                preparedStatement.executeUpdate();

            }
            catch(SQLException e){
                throw new DataAccessException("failed to get connection");
            }
    }

    public void deleteAuth(AuthData authData){
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("DELETE FROM auth WHERE username = ?")) {
            preparedStatement.setString(1, authData.username());
            preparedStatement.executeUpdate();

        }
        catch(SQLException e){
            throw new DataAccessException("failed to get connection");
        }
    }

    public boolean authTaken(String authT){
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("SELECT authToken FROM auth WHERE authToken = ?")) {
            preparedStatement.setString(1, authT);
            try (var rs = preparedStatement.executeQuery()){
                while(rs.next()){
                    var username = rs.getString("username");
                    var authToken = rs.getString("authToken");
                    if (authToken.equals(authT)){
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

    /*public String getUsername(String authToken){

    }

    /*public void clear(){

    }*/
}
