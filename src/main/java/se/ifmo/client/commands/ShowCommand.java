package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;

/**
 * The {@link ShowCommand} class represents a commandName that displays information about the current collection.
 * This commandName is used to show all the elements in the collection.
 */
public class ShowCommand extends Command {

    /**
     * Constructs a {@link ShowCommand}.
     * Initializes the commandName with the name "show" and the description
     * "show info about collection".
     */
    public ShowCommand() {
        super("show", "show info about collection");
    }

    /**
     * Executes the "show" commandName. This commandName retrieves and displays all elements in the collection.
     * If the collection is empty, it returns a message indicating that the collection is clear.
     *
     * @param request the request containing parameters for the commandName (though not used in this case)
     * @return a {@link Response} containing the collection information or a message if the collection is empty
     */
    @Override
    public Response execute(Request request) {
        CollectionManager collectionManager = CollectionManager.getInstance();

        // Check if the collection is empty
        if (collectionManager.getInstance().getDragons().isEmpty()) {
            return new Response("Your collection is clear");
        }

        // Build the response string for the collection elements
        StringBuilder result = new StringBuilder("Collection elements:\n");
        collectionManager.getDragons().forEach((id, dragon) ->
                result.append("ID: ").append(id)
                        .append(", ").append(dragon)
                        .append("\n")
        );
        CollectionManager.getInstance().sortDragons();
        // Return the collection information as a response
        return new Response(result.toString());
    }
}
