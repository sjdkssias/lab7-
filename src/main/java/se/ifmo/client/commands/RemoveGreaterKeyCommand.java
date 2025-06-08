package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;

/**
 * The {@link RemoveGreaterKeyCommand} class represents a commandName that removes all dragons from the collection
 * whose key is greater than a specified value.
 * This commandName is used to remove dragons whose ID (key) is greater than the ID entered by the user.
 */
public class RemoveGreaterKeyCommand extends Command {

    /**
     * Constructs a {@link RemoveGreaterKeyCommand}.
     * Initializes the commandName with the name "remove_greater_key" and the description
     * "remove all elements whose key is greater than your specified value".
     */
    public RemoveGreaterKeyCommand() {
        super("remove_greater_key", "remove all elements whose key greater than your", 0);
    }

    /**
     * Executes the "remove_greater_key" commandName. This commandName removes all dragons from the collection
     * whose key (ID) is greater than the value specified by the user.
     *
     * @param request the request containing parameters for the commandName (in this case, no arguments are required)
     * @return a {@link Response} containing a message indicating the result of the removal operation
     */
    @Override
    public Response execute(Request request) {
        if (request.args() == null) {
            return new Response("Request arguments are null");
        }
        Long id;

        try {
            id = Long.parseLong(request.args().get(0));
        } catch (NumberFormatException e) {
            return new Response("Invalid ID format. Please enter a valid integer.");
        }
        CollectionManager.getInstance().removeGreater(id, request.userRec().username());
        return new Response("Removed elements with key greater than " + id);
    }
}
