package se.ifmo.server.collectionManagement;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.ifmo.server.Server;
import se.ifmo.server.database.DragonService;

import se.ifmo.server.models.classes.Dragon;


@Getter
public class CollectionManager {
    private static Logger logger = LogManager.getLogger(CollectionManager.class);
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


    public boolean load() {
        try {
            List<Dragon> dragonDb = DragonService.getInctance().findAll();
            dragons.clear();
            for (Dragon dragon : dragonDb) {
                dragons.put(dragon.getId(), dragon);
            }
            return true;
        } catch (SQLException e) {
            logger.error("");
        }
        return false;
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
    public synchronized void removeById(long id) {
        try {
            if (DragonService.getInctance().removeById(id)){
                dragons.remove(id);
            }
        } catch (SQLException e){
            logger.error("nafine соси хуяку");
        }
    }



    public List<Dragon> sortDragons() {
        return dragons.values().stream()
                .sorted(Comparator.comparing(Dragon::getName))
                .collect(Collectors.toList());
    }
    /**
     * Adds a new dragon to the collection with a generated unique ID.
     *
     * @param dragon the dragon to add to the collection.
     */
    public boolean add(Dragon dragon) {
        try {
            long generatedId = DragonService.getInctance().addDragon(dragon);
            if (generatedId != -1) {
                dragon.setId(generatedId);
                dragons.put(generatedId, dragon);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Add to the database error: ", e);
        }
        return false;
    }

    /**
     * Retrieves the dragon with the highest ID in the collection.
     *
     * @return a list containing the dragon with the highest ID, or an empty list if the collection is empty.
     */
    public List<Dragon> getMaxByKey() {
        if (dragons.isEmpty()) {
            return Collections.emptyList();
        }
        long maxKey = dragons.lastKey();
        return List.of(dragons.get(maxKey));
    }
}
