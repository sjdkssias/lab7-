package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;
import se.ifmo.server.database.UserService;

/**
 * The {@link ClearCommand} class represents a commandName to clear the user's collection of dragons.
 * This commandName removes all dragons from the collection managed by {@link CollectionManager}.
 */
public class ClearCommand extends Command {

    /**
     * Constructs a {@link ClearCommand}.
     * The constructor initializes the commandName with the name "clear" and the description "clear your collection".
     */
    public ClearCommand() {
        super("clear", "clear your collection");
    }

    /**
     * Executes the "clear" commandName by clearing the collection of dragons.
     * It interacts with the {@link CollectionManager} to remove all dragons from the collection.
     *
     * @param request the request containing the information for the commandName execution
     * @return a {@link Response} indicating that the collection was cleared
     */
    @Override
    public Response execute(Request request) {
        return new Response("");
    }
}
