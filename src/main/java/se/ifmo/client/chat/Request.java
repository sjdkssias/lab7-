package se.ifmo.client.chat;

import se.ifmo.client.console.Console;
import se.ifmo.server.models.classes.Dragon;

import java.util.List;

/**
 * The {@link Request} record represents a command request made by the user.
 * It encapsulates the details of the command, its arguments, associated dragons, and the console.
 * This record is used for passing command-related data between the client and the server.
 */
public record Request(
        /**
         * The name of the command being executed.
         */
        String command,

        /**
         * A list of arguments associated with the command.
         */
        List<String> args,

        /**
         * A list of dragons associated with the command, if applicable.
         */
        List<Dragon> dragons

) {
}
