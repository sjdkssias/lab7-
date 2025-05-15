package se.ifmo.server.database;

import se.ifmo.server.models.classes.User;

import java.util.List;

public interface UserI {
    boolean checkPassword(User user, String pass);
    List<User> findAll();
}
