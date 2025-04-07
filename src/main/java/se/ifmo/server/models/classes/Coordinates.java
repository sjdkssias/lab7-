package se.ifmo.server.models.classes;

import lombok.*;
import se.ifmo.server.models.interfaces.Validatable;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates implements Validatable, Serializable {

    /**
     * The x-coordinate of the point.
     * This field cannot be null and must be a positive value.
     */
    @NonNull
    private Float x;

    /**
     * The y-coordinate of the point.
     * This field cannot be null and must be a positive value.
     */
    @NonNull
    private Long y;

    /**
     * Validates the coordinates to ensure that both the x and y values are greater than 0.
     *
     * @return true if both coordinates are greater than 0, otherwise false.
     */
    @Override
    public boolean validate() {
        if (x <= 0 || y <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of the coordinates in the format "(x, y)".
     *
     * @return a string representing the coordinates in the format "(x, y)".
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
