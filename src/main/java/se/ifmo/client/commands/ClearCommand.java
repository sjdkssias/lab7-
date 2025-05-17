package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.chat.UserRec;
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

    @Override
    public Response execute(Request request) {
        UserRec user = request.userRec();
        if (user == null || user.username() == null) {
            return new Response("Unauthorized: user not provided." + user);
        }

        CollectionManager.getInstance().removeUsersDragons(user.username());

        return new Response("Your collection has been cleared.");
    }
}
