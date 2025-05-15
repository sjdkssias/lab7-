package se.ifmo.server.database;

public class UserSQl {
    public static final String FIND_ALL = "SELECT * FROM users";
    public static final String FIND_BY_ID = "SELECT * FROM users WHERE uid = ?";
    public static final String SAVE = "INSERT INTO users (username, password) VALUES (?, ?)";
}
