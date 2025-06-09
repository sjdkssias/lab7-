package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;
import se.ifmo.server.models.classes.Dragon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        List<Dragon> dragons = new ArrayList<>(collectionManager.getDragons().values());

        if (dragons.isEmpty()) {
            return new Response(true, "Your collection is clear", Collections.emptyList());
        }

        return new Response(true, "Collection was received", dragons);
    }
}
