package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;

/**
 * The {@link RemoveLowerKeyCommand} class represents a commandName that removes all elements from the collection
 * whose key (ID) is less than the specified one.
 * This commandName is used to remove dragons whose key is less than the provided value.
 */
public class RemoveLowerKeyCommand extends Command {

    /**
     * Constructs a {@link RemoveLowerKeyCommand}.
     * Initializes the commandName with the name "remove_lower_key" and the description
     * "remove from the collection all elements whose key is less than the given one".
     */
    public RemoveLowerKeyCommand() {
        super("remove_lower_key", "remove from the collection all elements whose key is less than the given one", 0);
    }

    /**
     * Executes the "remove_lower_key" commandName. This commandName removes all elements from the collection
     * whose key (ID) is less than the value specified by the user.
     *
     * @param request the request containing parameters for the commandName (in this case, the ID threshold for removal)
     * @return a {@link Response} containing a message indicating the result of the removal operation
     */
    @Override
    public Response execute(Request request) {
        if (request.args() == null) {
            return new Response("You must provide the ID of the dragon to remove.");
        }

        long id;
        try {
            id = Long.parseLong(request.args().get(0));
        } catch (NumberFormatException e) {
            return new Response("Invalid ID format");
        }

        CollectionManager.getInstance().removeLower(id, request.userRec().username());

        return new Response("Removed elements with key greater than " + id);
    }
}

