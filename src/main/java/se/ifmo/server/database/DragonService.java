package se.ifmo.server.database;

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
                dragons.add(dragonMap(dragonResult));
            }
        } catch (SQLException e) {
            Server.logger.error("Error with getting dragons from db" + e.getMessage(), e);
            System.out.println("huinia");
        }
        return dragons;
    }

    @Override
    public Dragon findByID(long id) {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.FIND_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return dragonMap(rs);
                }
            }
        } catch (SQLException e) {
            Server.logger.error("find by id error: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public long addDragon(Dragon dragon) {
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
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getLong(1);
            return -1;
        } catch (SQLException e) {
            System.out.println("add dragon to the db error:");
            Server.logger.error("add dragon to the db error: " + e.getMessage(), e);
            return -1;
        }
    }

    @Override
    public boolean removeById(long id) {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.REMOVE_DRAGON_BY_ID)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Server.logger.error("remove by id error : " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public long removeUsersDragons(long uid) {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.REMOVE_USERS_DRAGON)) {
            stmt.setLong(1, uid);
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
        return dragon;
    }
}
