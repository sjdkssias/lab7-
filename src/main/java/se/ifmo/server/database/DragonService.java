package se.ifmo.server.database;

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

public class DragonService implements DragonI{
    private static DragonService instance;


    public static DragonService getInctance(){
        return instance == null ? instance = new DragonService() : instance;
    }

    @Override
    public List<Dragon> findAll() throws SQLException {
        List<Dragon> dragons = new ArrayList<>();
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.FIND_ALL);
             ResultSet dragonResult =  stmt.executeQuery()){
            while (dragonResult.next()) {
                dragons.add(dragonMap(dragonResult));
            }
        }
        return dragons;
    }

    @Override
    public Dragon findByID(long id) throws SQLException
    {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.FIND_BY_ID))
            {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return dragonMap(rs);
                }
            }

        }
        return null;
    }

    @Override
    public long addDragon(Dragon dragon) throws SQLException{
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.ADD_DRAGON)){
            stmt.setLong(1, dragon.getId());
            stmt.setString(2, dragon.getName());
            stmt.setFloat(3, dragon.getCoordinates().getX());
            stmt.setLong(4, dragon.getCoordinates().getY());
            stmt.setBoolean(5, dragon.isSpeaking());
            stmt.setString(6, dragon.getColor().name());
            stmt.setString(7, dragon.getCharacter().name());
            stmt.setFloat(8, dragon.getHead().getToothcount());
            try (ResultSet dragonRes = stmt.executeQuery()) {
                if (dragonRes.next()) {
                    long id = dragonRes.getLong("id");
                    dragon.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    @Override
    public void removeById(long id) throws SQLException{
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.REMOVE_DRAGON_BY_ID)){
            stmt.setLong(1, id);
            stmt.executeQuery();
        }
    }

    @Override
    public long removeUsersDragons(long id) throws SQLException {
        try (PreparedStatement stmt = ConnectionManager.getInstance().prepare(DragonSQL.REMOVE_USERS_DRAGON)){
            stmt.setLong(1, id);
            return stmt.executeUpdate();
        }
    }

    private Dragon dragonMap(ResultSet dragonResult) throws SQLException{
        Dragon dragon = new Dragon();
        dragon.setId(dragonResult.getLong("id"));
        dragon.setName(dragonResult.getString("name"));
        dragon.setCoordinates(new Coordinates(dragonResult.getFloat("x"), dragonResult.getLong("y")));
        dragon.setSpeaking(dragonResult.getBoolean("speaking"));
        dragon.setColor(Color.valueOf(dragonResult.getString("color")));
        dragon.setCharacter(DragonCharacter.valueOf(dragonResult.getString("character")));
        dragon.setHead(new DragonHead(dragonResult.getFloat("toothcount")));
        return dragon;
    }
}
