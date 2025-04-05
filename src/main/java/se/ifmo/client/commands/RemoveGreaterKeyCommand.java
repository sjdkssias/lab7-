package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.console.Console;
import se.ifmo.server.CollectionManager;
import se.ifmo.server.models.classes.Dragon;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * The {@link RemoveGreaterKeyCommand} class represents a command that removes all dragons from the collection
 * whose key is greater than a specified value.
 * This command is used to remove dragons whose ID (key) is greater than the ID entered by the user.
 */
public class RemoveGreaterKeyCommand extends Command {

    /**
     * Constructs a {@link RemoveGreaterKeyCommand}.
     * Initializes the command with the name "remove_greater_key" and the description
     * "remove all elements whose key is greater than your specified value".
     */
    public RemoveGreaterKeyCommand() {
        super("remove_greater_key", "remove all elements whose key greater than your", 0);
    }

    /**
     * Executes the "remove_greater_key" command. This command removes all dragons from the collection
     * whose key (ID) is greater than the value specified by the user.
     *
     * @param request the request containing parameters for the command (in this case, no arguments are required)
     * @return a {@link Response} containing a message indicating the result of the removal operation
     */
    @Override
    public Response execute(Request request) {
        if (request.args() == null) {
            return new Response("Request arguments are null");
        }

        // Initialize console to get user input
        Console console = new Console();

        // Prompt the user to enter an ID to compare
        console.write("Enter the ID for the new dragon (Integer value):");
        String idString = console.read();
        Long id;

        // Parse the entered ID
        try {
            id = Long.parseLong(idString);
        } catch (NumberFormatException e) {
            return new Response("Invalid ID format. Please enter a valid integer.");
        }

        // Get the collection of dragons
        TreeMap<Long, Dragon> collection = CollectionManager.getInstance().getDragons();

        // Find the keys that are greater than the entered ID
        List<Long> keysToRemove = new ArrayList<>(collection.tailMap(id, false).keySet());

        // If no elements with greater keys are found, return a message indicating so
        if (keysToRemove.isEmpty()) {
            return new Response("No elements found with key greater than " + id);
        }

        // Remove dragons with keys greater than the entered ID
        for (long key : keysToRemove) {
            collection.remove(key);
        }

        // Return a message indicating how many elements were removed
        return new Response("Removed " + keysToRemove.size() + " elements with key greater than " + id);
    }
}
