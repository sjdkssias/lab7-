package se.ifmo.server.database;

public class UserSQl {
    public static final String REGISTER = "INSERT INTO users (username, password) VALUES (?, ?)";
    public static final String FIND_BY_NAME = "SELECT * FROM users WHERE username = ?";
    public static final String LOGIN = "SELECT password FROM users WHERE username = ?";
}
