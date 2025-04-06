package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;

/**
 * The {@link UpdateIdCommand} class represents a command that updates the value of an existing
 * collection element identified by its ID.
 * This command allows the user to update the value of the dragon with the given ID.
 */
public class UpdateIdCommand extends Command {

    /**
     * Constructs a {@link UpdateIdCommand}.
     * Initializes the command with the name "update" and the description
     * "update the value of a collection element whose id is equal to a given one".
     */
    public UpdateIdCommand() {
        super("update", "update the value of a collection element whose id is equal to a given one");
    }

    /**
     * Executes the "update" command. This command updates the value of the dragon with the specified ID
     * in the collection. If the ID does not exist, the command returns an error message.
     * The method also prompts the user to input the dragon's ID and validates the input.
     *
     * @param request the request containing parameters for the command, including the dragon's ID
     *                and the dragon object to update.
     * @return a {@link Response} containing the result of the update operation or an error message
     */
    @Override
    public Response execute(Request request) {
        // Check if the arguments are provided
        if (request.args() == null) {
            return new Response("null request");
        }


        Long id;
        try {
            // Parse the ID
            id = Long.parseLong(request.args().get(0));
        } catch (NumberFormatException e) {
            // Handle invalid ID format
            return new Response("Invalid ID format. Please enter a valid integer.");
        }

        // Check if the dragon with the specified ID exists in the collection
        if (!CollectionManager.getInstance().containsId(id)) {
            return new Response("Dragon with this ID doesn't exist!");
        }

        // Replace the dragon with the new value in the collection
        CollectionManager.getInstance().getDragons().replace(id, request.dragons().get(0));

        // Return success message
        return new Response("Dragon with ID " + id + " was saved to collection with new value");
    }
}
