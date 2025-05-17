package se.ifmo.server.database;

import se.ifmo.server.models.classes.Dragon;

import java.sql.SQLException;
import java.util.List;

public interface DragonI {
    List<Dragon> findAll() throws SQLException;
    Dragon findByID(long id);
    long addDragon(Dragon dragon) ;
    boolean removeById(long id);
    long removeUsersDragons(long uid);
}
