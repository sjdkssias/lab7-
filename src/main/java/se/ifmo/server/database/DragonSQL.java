package se.ifmo.server.database;
import lombok.*;
@Getter
public class DragonSQL {
    public static final String FIND_BY_ID = "SELECT * FROM dragon WHERE id = ?";
    public static final String FIND_ALL = "SELECT * FROM dragon";
    public static final String ADD_DRAGON = "INSERT INTO dragon (name, x, y, speaking, color, character, head, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?) returning id";
    public static final String REMOVE_DRAGON_BY_ID = "DELETE FROM dragon WHERE id = ?";
    public static final String REMOVE_USERS_DRAGON = "DELETE FROM dragon WHERE user_id = ?";
    
}
