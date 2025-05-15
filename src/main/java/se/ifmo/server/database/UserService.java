package se.ifmo.server.database;


import se.ifmo.server.models.classes.User;

import java.util.List;

import static se.ifmo.server.crypt.Crypt.hashPassword;

public class UserService implements UserI{
    @Override
    public boolean checkPassword(User user, String password) {
        if (password == null || user == null) return false;
        return hashPassword(password).equals(user.getPassword());
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}



