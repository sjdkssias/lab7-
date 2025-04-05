package se.ifmo.client.chat;

import se.ifmo.server.models.classes.Dragon;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * The {@link Response} record represents the response sent after processing a command.
 * It contains a message with additional data (such as dragons) if required.
 * This is the result returned from executing a command and can be sent back to the user.
 */
public record Response(
        /**
         * The message accompanying the response, typically a status or output from the executed command.
         */
        String message,

        /**
         * A list of dragons associated with the response, if applicable.
         * This could include dragons retrieved, added, or modified based on the executed command.
         */
        List<Dragon> dragons
) implements Serializable {

    /**
     * Constructor to create a response with a message and a list of dragons.
     *
     * @param message The message to be included in the response.
     * @param dragons The list of dragons to be included in the response.
     */
    public Response(String message, Dragon... dragons) {
        this(message, List.of(dragons));
    }

    /**
     * Constructor to create a response with a message and no dragons.
     *
     * @param message The message to be included in the response.
     */
    public Response(String message){
        this(message, Collections.emptyList());
    }

    /**
     * Creates an empty response with a null message and no dragons.
     *
     * @return An empty response.
     */
    public static Response empty(){
        return new Response(null);
    }

    /**
     * Retrieves the message from the response.
     *
     * @return The message of the response.
     */
    public String getMessage(){
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }
}

