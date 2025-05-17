package se.ifmo.client.chat;

import se.ifmo.server.models.classes.Dragon;

import java.io.Serializable;
import java.util.List;

/**
 * The {@link Request} record represents a commandName request made by the user.
 * It encapsulates the details of the commandName, its arguments, associated dragons, and the console.
 * This record is used for passing commandName-related data between the client and the server.
 */
public record Request(
        /**
         * The name of the commandName being executed.
         */
        String commandName,

        /**
         * A list of arguments associated with the commandName.
         */
        List<String> args,

        /**
         * A list of dragons associated with the commandName, if applicable.
         */
        List<Dragon> dragons,

        UserReq userReq

) implements Serializable {}
