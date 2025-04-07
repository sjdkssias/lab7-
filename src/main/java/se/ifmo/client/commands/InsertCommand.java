package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;

import java.io.IOException;

/**
 * The {@link InsertCommand} class represents a commandName that allows the user to insert a dragon into the collection
 * with a specified ID.
 * The user is prompted to enter a unique ID for the new dragon, and if the ID is valid and unique,
 * the dragon is added to the collection.
 */
public class InsertCommand extends Command {

    /**
     * Constructs an {@link InsertCommand}.
     * Initializes the commandName with the name "insert", the description "add element with your key",
     * and requires 1 element (a dragon) to be added.
     */
    public InsertCommand() {
        super("insert", "add element with your key", 1);
    }

    /**
     * Executes the "insert" commandName. This commandName prompts the user to enter an ID for the new dragon
     * and inserts the dragon into the collection if the ID is valid and not already in use.
     *
     * @param request the request containing the dragon to be inserted
     * @return a {@link Response} indicating the result of the insert operation:
     *         success or failure (e.g., invalid ID format, or the ID already exists)
     * @throws IOException if an I/O error occurs during the interaction with the console
     */
    @Override
    public Response execute(Request request)  {
        // If there are no dragons in the request, return a message indicating the absence of dragons
        if (request.dragons() == null || request.dragons().isEmpty()) {
            return new Response("No dragons to add");
        }

        Long id;

        // Try to parse the entered ID as a Long, catch any parsing errors
        try {
            id = Long.parseLong(request.args().get(0));
        } catch (NumberFormatException e) {
            return new Response("Invalid ID format. Please enter a valid integer.");
        }

        // Check if the ID already exists in the collection
        if (CollectionManager.getInstance().containsId(id)) {
            return new Response("Dragon with this ID already exists!");
        }

        // Add the dragon to the collection with the specified ID
        CollectionManager.getInstance().getDragons().put(id, request.dragons().get(0));

        // Return a response confirming the dragon was added with the specified ID
        return new Response("Dragon was saved to collection with ID: " + id);
    }
}

