package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.console.Console;
import se.ifmo.server.collectionManagement.CollectionManager;

/**
 * The {@link RemoveKeyCommand} class represents a commandName that removes all elements from the collection
 * with a specified key (ID).
 * This commandName is used to remove all dragons whose key matches the given ID.
 */
public class RemoveKeyCommand extends Command {

    /**
     * Constructs a {@link RemoveKeyCommand}.
     * Initializes the commandName with the name "remove_key" and the description
     * "remove all elements who have the given key".
     */
    public RemoveKeyCommand() {
        super("remove_key", "remove all elements who have the given key");
    }

    /**
     * Executes the "remove_key" commandName. This commandName removes all elements from the collection
     * whose key (ID) matches the value specified by the user.
     *
     * @param request the request containing parameters for the commandName (in this case, the ID to remove)
     * @return a {@link Response} containing a message indicating the result of the removal operation
     */
    @Override
    public Response execute(Request request) {
        // Check if arguments are provided in the request
        if (request.args() == null) {
            return new Response("Request arguments are null");
        }

        // Initialize console to get user input
        Console console = new Console();

        // Prompt the user to enter an ID to remove
        console.write("Enter the ID for the dragon (Integer value):");
        String idString = console.read();
        Long id;

        // Parse the entered ID
        try {
            id = Long.parseLong(idString);
        } catch (NumberFormatException e) {
            return new Response("Invalid ID format. Please enter a valid integer.");
        }

        // Remove elements from the collection by the given ID
        CollectionManager.getInstance().removeById(id);

        // Return a message indicating how many elements were removed
        return new Response("Elements with key " + id + " were removed.");
    }
}
