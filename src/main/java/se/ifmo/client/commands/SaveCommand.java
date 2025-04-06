package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;

/**
 * The {@link SaveCommand} class represents a command that saves the collection to a file.
 * This command is used to persist the collection data by saving it to a file.
 */
public class SaveCommand extends Command {

    /**
     * Constructs a {@link SaveCommand}.
     * Initializes the command with the name "save" and the description
     * "save collection to file".
     */
    public SaveCommand() {
        super("save", "save collection to file");
    }

    /**
     * Executes the "save" command. This command saves the current collection to a file
     * using the {@link CollectionManager}'s save method.
     *
     * @param request the request containing parameters for the command (though not used in this case)
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
