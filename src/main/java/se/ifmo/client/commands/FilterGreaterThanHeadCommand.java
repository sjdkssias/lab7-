package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;
import se.ifmo.server.models.classes.Dragon;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@link FilterGreaterThanHeadCommand} class represents a command that filters and displays dragons
 * whose head's toothcount is greater than a specified value.
 * The command accepts a numeric value (toothcount) and filters the dragons whose head's toothcount is greater.
 */
public class FilterGreaterThanHeadCommand extends Command {

    /**
     * Constructs a {@link FilterGreaterThanHeadCommand}.
     * Initializes the command with the name "filter_greater" and description "display elements whose head field value is greater than the specified one".
     */
    public FilterGreaterThanHeadCommand() {
        super("filter_greater", "display elements whose head field value is greater than the specified one");
    }

    /**
     * Executes the "filter_greater" command. This command filters dragons based on their head's toothcount.
     * It compares the toothcount of each dragon's head with the value provided by the user and returns a list of dragons
     * whose head's toothcount is greater than the specified value.
     *
     * @param request the request containing the arguments for the command execution, including the head value to compare
     * @return a {@link Response} containing a message with the filtered dragons' information or an error message
     */
    @Override
    public Response execute(Request request) {
        // Check if the user provided the head value
        if (request.args().isEmpty()) {
            return new Response("You didn't write the head value");
        }

        // Parse the head value from the request
        float yourHead = Float.parseFloat(request.args().get(0));

        // Filter dragons with a head's toothcount greater than the provided value
        List<Dragon> filteredDragons = CollectionManager.getInstance().getDragons().values().stream()
                .filter(dragon -> dragon.getHead().getToothcount() > yourHead)
                .collect(Collectors.toList());

        // Check if any dragons were found
        if (filteredDragons.isEmpty()) {
            return new Response("No dragons with greater head");
        }

        // Return the list of filtered dragons as a response
        return new Response(String.valueOf(filteredDragons));
    }
}

