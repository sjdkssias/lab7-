package se.ifmo.server.collectionManagement;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import lombok.Getter;
import org.apache.logging.log4j.Level;
import se.ifmo.server.Server;
import se.ifmo.server.database.DragonService;

import se.ifmo.server.models.classes.Dragon;


@Getter
public class CollectionManager {
    /**
     * The singleton instance of the {@link CollectionManager}.
     */
    private static CollectionManager instance;

    /**
     * A {@link TreeMap} storing dragons, where the key is the unique dragon ID.
     * This collection allows for sorted access to dragons by their IDs.
     */
    private final TreeMap<Long, Dragon> dragons = new TreeMap<>();

    /**
     * Private constructor to initialize the collection manager and load the data.
     * This constructor is only accessible within the class and ensures the data is loaded upon instantiation.
     */
    private CollectionManager() {
        load();
    }

    /**
     * Retrieves the singleton instance of the {@link CollectionManager}.
     * If the instance does not exist, it is created and initialized.
     *
     * @return the singleton instance of the collection manager.
     */
    public static CollectionManager getInstance() {
        if (instance == null) {
            instance = new CollectionManager();
        }
        return instance;
    }


    public synchronized boolean load() {
        try {
            List<Dragon> dragonDb = DragonService.getInctance().findAll();
            dragons.clear();
            for (Dragon dragon : dragonDb) {
                dragons.put(dragon.getId(), dragon);
            }
            return true;
        } catch (SQLException e) {
            Server.logger.error("error to load collection" + e.getMessage());
            return false;
        }

    }

    /**
     * Checks if a dragon with the specified ID exists in the collection.
     *
     * @param id the ID of the dragon to check.
     * @return true if the dragon exists, otherwise false.
     */
    public boolean containsId(long id) {
        return dragons.containsKey(id);
    }

    /**
     * Removes the dragon with the specified ID from the collection.
     *
     * @param id the ID of the dragon to remove.
     */
    public synchronized void removeById(long id, String ownerName) {
        if (DragonService.getInctance().removeById(id, ownerName)) {
            dragons.entrySet().removeIf(entry -> entry.getKey() == id && ownerName.equals(entry.getValue().getOwnerName()));
        }
    }



    public synchronized List<Dragon> sortDragons() {
        return dragons.values().stream()
                .sorted(Comparator.comparing(Dragon::getName))
                .collect(Collectors.toList());
    }
    /**
     * Adds a new dragon to the collection with a generated unique ID.
     *
     * @param dragon the dragon to add to the collection.
     */
    public synchronized boolean add(Dragon dragon) {
        long generatedId = DragonService.getInctance().addDragon(dragon);
        if (generatedId != -1) {
            dragon.setId(generatedId);
            dragons.put(generatedId, dragon);
            return true;
        }
        return false;
    }

    public synchronized boolean update(long id, Dragon dragon, String ownerName){
        if (dragons.get(id) == null) {
            Server.logger.log(Level.WARN, "Update failed: dragon with id " + id + " not found.");
            return false;
        }
        if (!ownerName.equals(dragons.get(id).getOwnerName())) {
            Server.logger.log(Level.WARN, "Update failed: user " + ownerName + " tried to update dragon owned by " + dragon.getOwnerName());
            return false;
        }
        dragon.setId(id);
        dragon.setOwnerName(ownerName);
        boolean updatedInDb = DragonService.getInctance().update(dragon);
        if (updatedInDb) {
            dragons.put(id, dragon);
            return true;
        } else {
            Server.logger.log(Level.ERROR,"Update failed in DB for dragon id " + id);
            return false;
        }
    }


    public synchronized boolean insert(long id, Dragon dragon){
        dragon.setId(id);
        boolean insertedInDb = DragonService.getInctance().insert(dragon);

        if (insertedInDb) {
            dragons.put(id, dragon);
            return true;
        } else {
            return false;
        }
    }
    /**
     * Retrieves the dragon with the highest ID in the collection.
     *
     * @return a list containing the dragon with the highest ID, or an empty list if the collection is empty.
     */
    public synchronized List<Dragon> getMaxByKey() {
        if (dragons.isEmpty()) {
            return Collections.emptyList();
        }
        long maxKey = dragons.lastKey();
        return List.of(dragons.get(maxKey));
    }

    public synchronized long removeUsersDragons(String ownerName){
        dragons.entrySet().removeIf(entry -> ownerName.equals(entry.getValue().getOwnerName()));
        return DragonService.getInctance().removeUsersDragons(ownerName);
    }

    public synchronized int removeGreater(long id, String ownerName){

        List<Long> fromdb = dragons.tailMap(id, false).entrySet().stream()
                .filter(entry -> Objects.equals(ownerName, entry.getValue().getOwnerName()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()).stream()
                .filter(key -> DragonService.getInctance().removeById(key, ownerName))
                .collect(Collectors.toList());

        fromdb.forEach(dragons::remove);

        return fromdb.size();
    }

    public synchronized int removeLower(long id, String ownerName){
        List<Long> fromdb = dragons.headMap(id, false).entrySet().stream()
                .filter(entry -> Objects.equals(ownerName, entry.getValue().getOwnerName()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()).stream()
                .filter(key -> DragonService.getInctance().removeById(key, ownerName))
                .collect(Collectors.toList());

        fromdb.forEach(dragons::remove);

        return fromdb.size();
    }

}
