package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;

/**
 * The {@link MaxByIdCommand} class represents a command that returns the dragon from the collection
 * whose ID is the maximum among all the elements in the collection.
 * This command retrieves the dragon with the highest ID and displays it.
 */
public class MaxByIdCommand extends Command {

    /**
     * Constructs a {@link MaxByIdCommand}.
     * Initializes the command with the name "max_by_id" and the description
     * "show any object from the collection whose id field value is the maximum".
     */
    public MaxByIdCommand() {
        super("max_by_id", "show any object from the collection whose id field value is the maximum");
    }

    /**
     * Executes the "max_by_id" command. This command finds the dragon with the maximum ID value
     * in the collection and returns it as a response.
     *
     * @param request the request containing parameters for the command (not used in this case)
     * @return a {@link Response} containing the dragon with the highest ID in the collection
     */
    @Override
    public Response execute(Request request) {

        // Get the dragon with the maximum ID value from the collection
        CollectionManager.getInstance().getMaxByKey();

        // Return a response with the dragon having the max ID
        return new Response("Dragons with max id: " + CollectionManager.getInstance().getMaxByKey().toString());
    }
}
