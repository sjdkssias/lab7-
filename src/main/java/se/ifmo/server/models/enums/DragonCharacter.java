package se.ifmo.server.models.enums;

import java.io.Serializable;

/**
 * Enum representing the possible characters or alignments of a dragon.
 * The characters include WISE, EVIL, CHAOTIC, CHAOTIC_EVIL, and FICKLE.
 */
public enum DragonCharacter implements Serializable {

    /**
     * Represents a wise dragon character.
     */
    WISE,

    /**
     * Represents an evil dragon character.
     */
    EVIL,

    /**
     * Represents a chaotic dragon character.
     */
    CHAOTIC,

    /**
     * Represents a chaotic evil dragon character.
     */
    CHAOTIC_EVIL,

    /**
     * Represents a fickle dragon character.
     */
    FICKLE
}

