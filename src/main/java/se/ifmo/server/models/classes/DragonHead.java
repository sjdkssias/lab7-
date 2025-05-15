package se.ifmo.server.models.classes;

import lombok.*;

import java.io.Serializable;

/**
 * Represents the head of a dragon, containing the number of teeth.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DragonHead implements Serializable {

    /**
     * The number of teeth in the dragon's head.
     * This field is a floating-point number.
     */
    @Getter
    private float toothcount;
}
