package se.ifmo.server.database;


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
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.FIND_ALL);
            ResultSet result = stmt.executeQuery()) {
            while (result.next()){
                users.add(mapUser(result));
            }
            return users;
        }
    }

    @Override
    public void register(String name, String password) throws SQLException {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.REGISTER)){
            stmt.setString(1, name);
            stmt.setString(2, password);

            stmt.executeQuery();
        }
    }

    @Override
    public boolean login(String name, String password) throws SQLException {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.LOGIN)){
            stmt.setString(1, name);
            try (ResultSet result = stmt.executeQuery()){
                if (result.next()){
                    return result.getString("password").equals(hashPassword(password));
                }
                return false;
            }
        }
    }

    @Override
    public User findById(long uid) throws SQLException {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.FIND_BY_ID)){
            stmt.setLong(1, uid);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return mapUser(result);
                } return null;
            }
        }
    }

    @Override
    public User findByName(String name) throws SQLException {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.FIND_BY_ID)){
            stmt.setString(1, name);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return mapUser(result);
                } return null;
            }
        }
    }

    private User mapUser(ResultSet result) throws SQLException {
        User user = new User(result.getString("name"),result.getString("password") );
        return user;
    }
}



