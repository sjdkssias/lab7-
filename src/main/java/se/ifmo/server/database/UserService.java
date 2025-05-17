package se.ifmo.server.database;


import se.ifmo.server.Server;
import se.ifmo.server.models.classes.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static se.ifmo.server.crypt.Crypt.hashPassword;

public class UserService implements UserI{

    private static UserService instance;

    private UserService(){
    }

    public static UserService getInstance(){
        return instance == null ? instance = new UserService() : instance;
    }

    @Override
    public List<User> findAll(){
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.FIND_ALL);
            ResultSet result = stmt.executeQuery()) {
            while (result.next()) {
                users.add(mapUser(result));
            }
        } catch (SQLException e) {
            Server.logger.error("Error retrieving all users: " + e.getMessage(), e);
        }
        return users;
    }

    @Override
    public boolean register(String name, String password){
        if (findByName(name) != null) return false;
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.REGISTER)){
            stmt.setString(1, name);
            stmt.setString(2, hashPassword(password));
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Server.logger.error("Error registering user: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean login(String name, String password) {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.LOGIN)){
            stmt.setString(1, name);
            try (ResultSet result = stmt.executeQuery()){
                if (result.next()){
                    System.out.println(result.getString("password"));
                    System.out.println(hashPassword(password));
                    return result.getString("password").equals(hashPassword(password));
                }
            }
        } catch (SQLException e) {
            Server.logger.error("Login error for user '" + name + "': " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public User findById(long uid){
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.FIND_BY_ID)){
            stmt.setLong(1, uid);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return mapUser(result);
                } return null;
            } catch (SQLException e) {
                Server.logger.error("Error:" + e.getMessage());
                return null;
            }
        } catch (SQLException e) {
            Server.logger.error("Error:" + e.getMessage());
            return null;
        }
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
        User user = new User(result.getString("name"),result.getString("password") );
        return user;
    }
}



