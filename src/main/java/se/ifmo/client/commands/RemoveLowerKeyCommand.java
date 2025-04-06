package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.console.Console;
import se.ifmo.server.collectionManagement.CollectionManager;
import se.ifmo.server.models.classes.Dragon;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * The {@link RemoveLowerKeyCommand} class represents a command that removes all elements from the collection
 * whose key (ID) is less than the specified one.
 * This command is used to remove dragons whose key is less than the provided value.
 */
public class RemoveLowerKeyCommand extends Command {

    /**
     * Constructs a {@link RemoveLowerKeyCommand}.
     * Initializes the command with the name "remove_lower_key" and the description
     * "remove from the collection all elements whose key is less than the given one".
     */
    public RemoveLowerKeyCommand() {
        super("remove_lower_key", "remove from the collection all elements whose key is less than the given one", 0);
    }

    /**
     * Executes the "remove_lower_key" command. This command removes all elements from the collection
     * whose key (ID) is less than the value specified by the user.
     *
     * @param request the request containing parameters for the command (in this case, the ID threshold for removal)
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

        // Prompt the user to enter an ID to compare the keys
        console.write("Enter the ID (Integer value) for comparison:");
        String idString = console.read();
        Long id;

        // Parse the entered ID
        try {
            id = Long.parseLong(idString);
        } catch (NumberFormatException e) {
            return new Response("Invalid ID format. Please enter a valid integer.");
        }

        // Remove elements from the collection whose key is less than the specified ID
        TreeMap<Long, Dragon> collection = CollectionManager.getInstance().getDragons();
        List<Long> keysToRemove = new ArrayList<>(collection.headMap(id, false).keySet());

        // If no elements found with a key lower than the specified ID, return a message
        if (keysToRemove.isEmpty()) {
            return new Response("No elements found with key lower than " + id);
        }

        // Remove the elements from the collection
        for (long key : keysToRemove) {
            collection.remove(key);
        }

        // Return a message indicating how many elements were removed
        return new Response("Removed " + keysToRemove.size() + " elements with key lower than " + id);
    }
}

