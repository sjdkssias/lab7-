package se.ifmo.server.models.classes;

import lombok.*;

/**
 * Represents the head of a dragon, containing the number of teeth.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DragonHead {

    /**
     * The number of teeth in the dragon's head.
     * This field is a floating-point number.
     */
    private float toothcount;
}
