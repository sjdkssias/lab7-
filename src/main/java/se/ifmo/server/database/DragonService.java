package se.ifmo.server.database;

import org.apache.logging.log4j.Level;
import se.ifmo.server.Server;
import se.ifmo.server.models.classes.Coordinates;
import se.ifmo.server.models.classes.Dragon;
import se.ifmo.server.models.classes.DragonHead;
import se.ifmo.server.models.enums.Color;
import se.ifmo.server.models.enums.DragonCharacter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DragonService implements DragonI {
    private static DragonService instance;


    public static DragonService getInctance() {
        return instance == null ? instance = new DragonService() : instance;
    }

    @Override
    public List<Dragon> findAll() throws SQLException {
        List<Dragon> dragons = new ArrayList<>();
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.FIND_ALL);
             ResultSet dragonResult = stmt.executeQuery()) {
            while (dragonResult.next()) {
                Dragon dragon = dragonMap(dragonResult);
                Server.logger.log(Level.INFO, "Найден дракон: " + dragon.getName()); // <-- Лог
                dragons.add(dragon);
            }
        } catch (SQLException e) {
            Server.logger.error("Error with getting dragons from db" + e.getMessage(), e);
        }
        return dragons;
    }


    @Override
    public Dragon findByName(String ownerName) {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.FIND_BY_NAME)) {
            stmt.setString(1, ownerName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return dragonMap(rs);
                }
            }
        } catch (SQLException e) {
            Server.logger.error("find by name error: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public long addDragon(Dragon dragon) {
        if (dragon.getOwnerName() == null) {
            Server.logger.log(Level.WARN, "trying to add a dragon without an owner");
            return -1;
        }
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.ADD_DRAGON)) {
            stmt.setString(1, dragon.getName());
            stmt.setFloat(2, dragon.getCoordinates().getX());
            stmt.setLong(3, dragon.getCoordinates().getY());
            stmt.setBoolean(4, dragon.isSpeaking());
            stmt.setString(5, dragon.getColor().name());
            if (dragon.getCharacter() != null) {
                stmt.setString(6, dragon.getCharacter().name());
            } else {
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }
            stmt.setFloat(7, dragon.getHead().getToothcount());
            stmt.setString(8, dragon.getOwnerName());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getLong(1);
            return -1;
        } catch (SQLException e) {
            Server.logger.error("add dragon to the db error: " + e.getMessage(), e);
            return -1;
        }
    }

    @Override
    public boolean removeById(long id, String ownerName) {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.REMOVE_DRAGON_BY_ID)) {
            stmt.setLong(1, id);
            stmt.setString(2, ownerName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Server.logger.error("remove by id error : " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean update(Dragon dragon) {
        if (dragon.getOwnerName() == null) {
            Server.logger.log(Level.WARN, "trying to add a dragon without an owner");
            return false;
        }
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.UPDATE_ID)) {
            stmt.setString(1, dragon.getName());
            stmt.setFloat(2, dragon.getCoordinates().getX());
            stmt.setLong(3, dragon.getCoordinates().getY());
            stmt.setBoolean(4, dragon.isSpeaking());
            stmt.setString(5, dragon.getColor().name());
            if (dragon.getCharacter() != null) {
                stmt.setString(6, dragon.getCharacter().name());
            } else {
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }
            stmt.setFloat(7, dragon.getHead().getToothcount());
            stmt.setLong(8, dragon.getId());
            stmt.setString(9, dragon.getOwnerName());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Server.logger.error("update dragon error: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean insert(Dragon dragon) {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.INSERT)) {
            stmt.setString(1, dragon.getName());
            stmt.setFloat(2, dragon.getCoordinates().getX());
            stmt.setLong(3, dragon.getCoordinates().getY());
            stmt.setBoolean(4, dragon.isSpeaking());
            stmt.setString(5, dragon.getColor().name());
            if (dragon.getCharacter() != null) {
                stmt.setString(6, dragon.getCharacter().name());
            } else {
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }
            stmt.setFloat(7, dragon.getHead().getToothcount());
            stmt.setLong(8, dragon.getId());
            stmt.setString(9, dragon.getOwnerName());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Server.logger.error("insert dragon error: " + e.getMessage(), e);
            return false;
        }
    }


    @Override
    public long removeUsersDragons(String ownerName) {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.REMOVE_USERS_DRAGON)) {
            stmt.setString(1, ownerName);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            Server.logger.error("remove users dragons error : " + e.getMessage(), e);
            return 0;
        }
    }

    private Dragon dragonMap(ResultSet dragonResult) throws SQLException {
        Dragon dragon = new Dragon();
        dragon.setId(dragonResult.getLong("id"));
        dragon.setName(dragonResult.getString("name"));
        dragon.setCoordinates(new Coordinates(dragonResult.getFloat("x"), dragonResult.getLong("y")));
        dragon.setSpeaking(dragonResult.getBoolean("speaking"));
        dragon.setColor(Color.valueOf(dragonResult.getString("color")));
        if (dragonResult.getString("character") != null && !dragonResult.getString("character").isEmpty()) {
            try {
                dragon.setCharacter(DragonCharacter.valueOf(dragonResult.getString("character")));
            } catch (IllegalArgumentException e) {
                dragon.setCharacter(DragonCharacter.UNKNOWN);
            }
        }
        dragon.setHead(new DragonHead(dragonResult.getFloat("head")));
        dragon.setOwnerName(dragonResult.getString("owner_name"));
        return dragon;
    }
}
