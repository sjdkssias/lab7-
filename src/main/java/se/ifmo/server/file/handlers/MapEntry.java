package se.ifmo.server.file.handlers;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import se.ifmo.server.models.classes.Dragon;

/**
 * Represents an entry in the map, consisting of a key-value pair where the key is a {@link Long}
 * and the value is a {@link Dragon}.
 * This class is annotated with Jackson annotations to support XML serialization and deserialization.
 *
 * The key is a unique identifier for the dragon, and the value is the corresponding dragon object.
 */
@Getter
@Setter
@JacksonXmlRootElement(localName = "entry")
public class MapEntry {

    /**
     * The key of the map entry, representing a unique identifier for the dragon.
     */
    @JacksonXmlProperty(localName = "key")
    private long key;

    /**
     * The value of the map entry, representing the dragon associated with the key.
     */
    @JacksonXmlProperty(localName = "value")
    private Dragon value;

    /**
     * Default constructor for creating an empty map entry.
     */
    public MapEntry() {}

    /**
     * Constructor for creating a map entry with a specified key and dragon value.
     *
     * @param key the unique identifier for the dragon.
     * @param value the dragon object associated with the key.
     */
    public MapEntry(long key, Dragon value) {
        this.key = key;
        this.value = value;
    }
}
