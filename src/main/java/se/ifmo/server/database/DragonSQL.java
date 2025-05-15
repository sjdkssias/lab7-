package se.ifmo.server.database;
import lombok.*;
@Getter
public class DragonSQL {
    public static final String FIND_BY_ID = "SELECT * FROM dragons WHERE id = ?";
    public static final String FIND_ALL = "SELECT * FROM dragons";
    public static final String ADD_DRAGON = "INSERT INTO dragons (id, name, x, y, speaking, color, character, head) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    public static final String REMOVE_DRAGON_BY_ID = "DELETE FROM dragons WHERE id = ?";
    public static final String REMOVE_USERS_DRAGON = "DELETE FROM dragons WHERE user_id = ?";
    
}
