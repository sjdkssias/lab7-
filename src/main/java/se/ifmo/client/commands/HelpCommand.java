package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;

/**
 * The {@link HelpCommand} class represents a commandName that displays the description of all available commands.
 * This commandName shows a list of commands and their associated descriptions to the user.
 */
public class HelpCommand extends Command {

    /**
     * Constructs a {@link HelpCommand}.
     * Initializes the commandName with the name "help" and description "show description of all commands".
     */
    public HelpCommand() {
        super("help", "show description of all commands");
    }

    /**
     * Executes the "help" commandName. This commandName retrieves and returns the list of all available commands along
     * with their descriptions. It allows the user to view all possible commands and what they do.
     *
     * @param request the request containing any arguments for the commandName execution (not used in this case)
     * @return a {@link Response} containing a formatted string with the descriptions of all available commands
     */
    @Override
    public Response execute(Request request) {
        StringBuilder responseText = new StringBuilder("Available commands:\n");

        // Iterate over all commands and append their name and description to the response
        for (Command command : AllCommands.ALLCOMANDS) {
            responseText.append(command.getName())
                    .append(" - ")
                    .append(command.getDescription())
                    .append("\n");
        }

        // Return the formatted string of commands and descriptions as a response
        return new Response(responseText.toString());
    }
}
