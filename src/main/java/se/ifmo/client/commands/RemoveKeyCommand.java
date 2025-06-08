package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
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
        if (request.args() == null) {
            return new Response("You must provide the ID of the dragon to remove.");
        }

        long id;
        try {
            id = Long.parseLong(request.args().get(0));
        } catch (NumberFormatException e) {
            return new Response("Invalid ID format");
        }

        CollectionManager.getInstance().removeById(id, request.userRec().username());

        return new Response("Dragon with ID " + id + " was removed (if it existed and belonged to you).");
    }
}
