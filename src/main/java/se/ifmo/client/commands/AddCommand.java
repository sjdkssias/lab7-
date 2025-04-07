package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;

/**
 * The {@link AddCommand} class represents a commandName that adds a dragon to the collection.
 * This class extends the {@link Command} class and implements the logic to add a dragon
 * from the given request to the collection managed by the {@link CollectionManager}.
 *
 * <p>This commandName checks if there are dragons in the request, and if so, it adds the first dragon
 * to the collection. If no dragons are provided in the request, a response indicating that
 * no dragons are available for addition is returned.</p>
 */
public class AddCommand extends Command {

    /**
     * Constructs an {@link AddCommand} with predefined name ("add"), description ("add dragon to collection"),
     * and element number (1).
     */
    public AddCommand() {
        super("add", "add dragon to collection", 1);
    }

    /**
     * Executes the commandName to add a dragon to the collection.
     * It checks if the provided request contains dragons and adds the first one to the collection if present.
     * If no dragons are provided, it returns a response indicating that no dragons are available for addition.
     *
     * @param request the request containing dragons to be added to the collection.
     * @return a {@link Response} indicating whether the dragon was added or if there were no dragons to add.
     */
    @Override
    public Response execute(Request request) {
        if (request.dragons() == null || request.dragons().isEmpty()) {
            return new Response("No dragons to add");
        }
        CollectionManager.getInstance().add(request.dragons().get(0));
        return new Response("Dragon was saved to collection");
    }
}
