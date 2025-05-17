package se.ifmo.server.database;
import lombok.*;
@Getter
public class DragonSQL {
    public static final String FIND_BY_NAME = "SELECT * FROM dragon WHERE owner_name = ?";
    public static final String FIND_ALL = "SELECT * FROM dragon";
    public static final String ADD_DRAGON = "INSERT INTO dragon (name, x, y, speaking, color, character, head, owner_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?) returning id";
    public static final String REMOVE_DRAGON_BY_ID = "DELETE FROM dragon WHERE id = ? and owner_name = ?";
    public static final String REMOVE_USERS_DRAGON = "DELETE FROM dragon WHERE owner_name = ?";
    
}
