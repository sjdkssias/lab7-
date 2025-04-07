package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;

/**
 * The {@link SaveCommand} class represents a commandName that saves the collection to a file.
 * This commandName is used to persist the collection data by saving it to a file.
 */
public class SaveCommand extends Command {

    /**
     * Constructs a {@link SaveCommand}.
     * Initializes the commandName with the name "save" and the description
     * "save collection to file".
     */
    public SaveCommand() {
        super("save", "save collection to file");
    }

    /**
     * Executes the "save" commandName. This commandName saves the current collection to a file
     * using the {@link CollectionManager}'s save method.
     *
     * @param request the request containing parameters for the commandName (though not used in this case)
     * @return a {@link Response} indicating that the collection was successfully saved to a file
     */
    @Override
    public Response execute(Request request) {
        // Save the collection to the file
        CollectionManager.getInstance().save();

        // Return a response indicating that the collection was saved
        return new Response("Collection was saved to the file");
    }
}
