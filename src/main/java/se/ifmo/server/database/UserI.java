package se.ifmo.server.database;

import se.ifmo.client.chat.UserRec;
import se.ifmo.server.models.classes.User;

public interface UserI {
    boolean register(UserRec req);

    User findByName(String name);

    boolean login(UserRec req);

    boolean logout(UserRec req);
}
