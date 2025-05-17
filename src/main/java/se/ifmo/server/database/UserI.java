package se.ifmo.server.database;

import se.ifmo.client.chat.UserReq;
import se.ifmo.server.models.classes.User;

import java.util.List;

public interface UserI {
    List<User> findAll();

    boolean register(UserReq req);

    User findById(long id);

    User findByName(String name);

    boolean login(UserReq req);
}
