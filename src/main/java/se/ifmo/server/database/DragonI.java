package se.ifmo.server.database;

import se.ifmo.server.models.classes.Dragon;

import java.sql.SQLException;
import java.util.List;

public interface DragonI {
    List<Dragon> findAll() throws SQLException;
    Dragon findByID(long id) throws SQLException;
    long addDragon(Dragon dragon) throws SQLException;
    void removeById(long id) throws SQLException;
    long removeUsersDragons(long id) throws SQLException;
}
