package se.ifmo.server.database;


import org.apache.logging.log4j.Level;
import se.ifmo.client.chat.UserRec;
import se.ifmo.server.Server;
import se.ifmo.server.models.classes.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserService implements UserI{

    private static UserService instance;

    private UserService(){
    }

    public static UserService getInstance(){
        return instance == null ? instance = new UserService() : instance;
    }


    @Override
    public boolean register(UserRec req){
        if (findByName(req.username()) != null) return false;
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.REGISTER)){
            stmt.setString(1, req.username());
            stmt.setString(2, req.password());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                Server.logger.log(Level.WARN, "Registration failed: username already exists: " + req.username());
            } else {
                Server.logger.log(Level.WARN, "Unexpected SQL error during registration: " + e.getMessage(), e);
            }
            return false;
        }
    }

    @Override
    public boolean login(UserRec user) {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.LOGIN)){
            stmt.setString(1, user.username());
            try (ResultSet result = stmt.executeQuery()){
                if (result.next()){
                    System.out.println(result.getString("password"));
                    System.out.println(user.password());
                    boolean r = result.getString("password").equals(user.password());
                    if (!r){
                        Server.logger.log(Level.INFO, "wrong password");
                    }
                    return r;
                }
            }
        } catch (SQLException e) {
            Server.logger.error("Login error for user '" + user.username() + "': " + e.getMessage(), e);
        }
        return false;
    }


    @Override
    public User findByName(String name) {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.FIND_BY_NAME)) {
            stmt.setString(1, name);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return mapUser(result);
                }
                return null;
            } catch (SQLException e) {
                Server.logger.error("SQL error :" + e.getMessage());
                return null;
            }
        } catch (SQLException e) {
            Server.logger.error("SQL error :" + e.getMessage());
            return null;
        }

    }

    private User mapUser(ResultSet result) throws SQLException {
        User user = new User(result.getString("name"),result.getString("password"));
        return user;
    }
}



