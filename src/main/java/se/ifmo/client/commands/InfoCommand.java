package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;
import se.ifmo.server.models.classes.Dragon;

import java.io.IOException;
import java.util.TreeMap;

/**
 * The {@link InfoCommand} class represents a command that provides information about the collection of dragons.
 * It retrieves and displays the type and size of the collection.
 */
public class InfoCommand extends Command {

    /**
     * Constructs an {@link InfoCommand}.
     * Initializes the command with the name "info" and description "information about collection".
     */
    public InfoCommand() {
        super("info", "information about collection");
    }

    /**
     * Executes the "info" command. This command retrieves information about the collection of dragons,
     * including the type and size of the collection.
     *
     * @param request the request containing any arguments for the command execution (not used in this case)
     * @return a {@link Response} containing information about the collection, such as its type and size
     * @throws IOException if an I/O error occurs while retrieving collection information
     */
    @Override
    public Response execute(Request request) throws IOException {
        // Retrieve the collection of dragons from the CollectionManager
        TreeMap<Long, Dragon> collection = CollectionManager.getInstance().getDragons();
        // Return the collection's type and size as a response
        return new Response("collection type: " + collection.getClass().getSimpleName() + "\n" + "collection size: " + collection.size());
    }
}
