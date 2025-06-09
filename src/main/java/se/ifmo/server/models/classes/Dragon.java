package se.ifmo.server.models.classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import se.ifmo.server.models.enums.Color;
import se.ifmo.server.models.enums.DragonCharacter;
import se.ifmo.server.models.interfaces.Validatable;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Getter
@EqualsAndHashCode
public class Dragon implements Comparable<Dragon>, Validatable, Serializable {

    /**
     * The unique identifier of the dragon.
     * This field is ignored during JSON serialization.
     */
    private long id;
    @NonNull
    private String ownerName;
    /**
     * The name of the dragon.
     * Cannot be null or empty.
     */
    @NonNull
    private String name;

    /**
     * The coordinates of the dragon.
     * Cannot be null and must be valid.
     */
    @NonNull
    private Coordinates coordinates;

    /**
     * The creation date of the dragon.
     * It is set to the current date if not explicitly provided.
     */
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime creationDate = LocalDateTime.now(); ;

    /**
     * Whether the dragon is capable of speaking.
     * It can be either true or false.
     */
    private boolean speaking;

    /**
     * The color of the dragon.
     * Cannot be null.
     */
    @NonNull
    private Color color;

    /**
     * The character of the dragon.
     * This field can be null.
     */
    private DragonCharacter character;

    /**
     * The head of the dragon, which contains information about the number of teeth.
     * Cannot be null.
     */
    @NonNull
    private DragonHead head;

    /**
     * Compares this dragon with another dragon by their IDs.
     *
     * @param o the dragon to be compared.
     * @return a negative integer, zero, or a positive integer as this dragon's ID is less than, equal to, or greater than the specified dragon's ID.
     */
    @Override
    public int compareTo(Dragon o) {
        return Long.compare(this.id, o.id);
    }

    /**
     * Validates the fields of the dragon.
     *
     * @return true if the dragon is valid, otherwise false.
     */
    @Override
    public boolean validate() {
        if (id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (creationDate == null) return false;
        if (coordinates == null || !coordinates.validate()) return false;
        if (speaking != true && speaking != false) return false;
        return true;
    }

    /**
     * Returns a string representation of the dragon.
     *
     * @return a string containing the dragon's name, coordinates, creation date, speaking status, color, character, and head information.
     */
    @Override
    public String toString() {
        return "Dragon:" +
                " name: '" + name + '\'' +
                ", coordinates :" + coordinates.toString() +
                ", creationDate :" + creationDate +
                ", speaking :" + speaking +
                ", color :" + color +
                ", character :" + character +
                ", head :" + head.getToothcount();
    }
}
