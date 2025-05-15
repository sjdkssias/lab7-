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
    public boolean checkPassword(User user, String password) {
        if (password == null || user == null) return false;
        return hashPassword(password).equals(user.getPassword());
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.FIND_ALL)) {

            return users;
        }
    }

    @Override
    public void save(String name, String password) throws SQLException {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.SAVE)){
            stmt.setString(1, name);
            stmt.setString(2, hashPassword(password));

            stmt.executeQuery();
        }
    }

    @Override
    public User findById(long id) throws SQLException {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(UserSQl.FIND_BY_ID)){
            stmt.setLong(1, id);

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



