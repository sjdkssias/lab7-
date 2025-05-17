package se.ifmo.server.database;

import se.ifmo.server.models.classes.User;

import java.util.List;

public interface UserI {
    List<User> findAll();

    boolean register(String name, String password);

    User findById(long id);

    User findByName(String name);

    boolean login(String name, String password);
}
