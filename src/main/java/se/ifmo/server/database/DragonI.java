package se.ifmo.server.database;

import se.ifmo.server.models.classes.Dragon;

import java.sql.SQLException;
import java.util.List;

public interface DragonI {
    List<Dragon> findAll() throws SQLException;
    Dragon findByName(String ownerName);
    long addDragon(Dragon dragon) ;
    boolean removeById(long id, String ownerName);
    long removeUsersDragons(String ownerName);
}
