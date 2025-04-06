package se.ifmo.server.collectionManagement;

import java.util.*;
import lombok.Getter;
import se.ifmo.server.file.handlers.XmlHandler;
import se.ifmo.server.models.classes.Dragon;

/**
 * Manages a collection of dragons stored in a {@link TreeMap} and handles operations
 * such as adding, removing, saving, and loading dragons.
 * The collection is uniquely identified by dragon IDs, which are generated using {@link #generateId()}.
 * This class follows the Singleton pattern, ensuring that only one instance of the collection manager exists.
 */
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

    /**
     * Generates a unique ID for a new dragon.
     * The ID is generated randomly and ensures it is unique by checking if it already exists in the collection.
     *
     * @return a unique long ID for a dragon.
     */
    public long generateId() {
        Random random = new Random();
        long newId;
        do {
            newId = random.nextLong();
        } while (dragons.containsKey(newId)); // Keep generating until a unique ID is found.
        return newId;
    }

    /**
     * Loads the dragon data from an XML file using the {@link XmlHandler}.
     * Clears the existing collection and reads the new data into the  map.
     *
     * @return true if the data was successfully loaded, otherwise false.
     */
    public boolean load() {
        try (XmlHandler xmlHandler = new XmlHandler()) {
            dragons.clear();
            dragons.putAll(xmlHandler.read());
            return true;
        } catch (NullPointerException e) {
            System.err.println("File is clear");
        }
        return false;
    }

    /**
     * Saves the current dragon collection to an XML file using the {@link XmlHandler}.
     *
     * @return true if the data was successfully saved, otherwise false.
     */
    public boolean save() {
        try (XmlHandler xmlHandler = new XmlHandler()) {
            xmlHandler.write(dragons);
            return true;
        } catch (Exception e) {
            System.err.println("Unknown error while saving: " + e.getMessage());
            e.printStackTrace();
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
    public void removeById(long id) {
        dragons.remove(id);
    }

    /**
     * Adds a new dragon to the collection with a generated unique ID.
     *
     * @param dragon the dragon to add to the collection.
     */
    public void add(Dragon dragon) {
        long k = generateId();
        dragons.put(k, dragon);
        dragon.setId(k);
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
