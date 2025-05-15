package se.ifmo.server.database;

import se.ifmo.server.models.classes.User;

import java.sql.SQLException;
import java.util.List;

public interface UserI {
    boolean checkPassword(User user, String pass) throws SQLException;
    List<User> findAll() throws SQLException;
    void save(String name, String password) throws SQLException;
    User findById(long id) throws SQLException;
}
