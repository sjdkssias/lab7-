package se.ifmo.client.chat;

import java.io.Serializable;

import static se.ifmo.server.crypt.Crypt.hashPassword;

public record UserRec(String username, String password) implements Serializable {
    public UserRec(String username, String password) {
        this.username = username;
        this.password = getHashPassword(password);
    }

    private String getHashPassword(String password) {
        if (password != null) {
            return hashPassword(password);
        }
        return null;
    }
}
