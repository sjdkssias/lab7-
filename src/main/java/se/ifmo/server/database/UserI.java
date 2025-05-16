package se.ifmo.server.database;

import se.ifmo.server.models.classes.User;

import java.sql.SQLException;
import java.util.List;

public interface UserI {
    List<User> findAll() throws SQLException;
    void register(String name, String password) throws SQLException;
    User findById(long id) throws SQLException;
    User findByName(String name) throws SQLException;
    boolean login(String name, String password) throws SQLException;
}
