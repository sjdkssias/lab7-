package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;
import se.ifmo.server.models.classes.Dragon;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@link PrintUniqueHeadCommand} class represents a commandName that finds and displays dragons
 * with unique head characteristics, specifically with a tooth count that occurs only once in the collection.
 * This commandName filters the collection of dragons to return only those whose head's tooth count is unique.
 */
public class PrintUniqueHeadCommand extends Command {

    /**
     * Constructs a {@link PrintUniqueHeadCommand}.
     * Initializes the commandName with the name "print_unique_head" and the description
     * "print unique dragon's head".
     */
    public PrintUniqueHeadCommand() {
        super("print_unique_head", "print unique dragon's head");
    }

    /**
     * Executes the "print_unique_head" commandName. This commandName filters the collection to find dragons
     * whose head's tooth count is unique (i.e., appears only once in the collection),
     * and returns a list of those dragons.
     *
     * @param request the request containing parameters for the commandName (not used in this case)
     * @return a {@link Response} containing a string representation of dragons with unique heads
     */
    @Override
    public Response execute(Request request) {

        // Group dragons by their head's tooth count and count occurrences
        Map<Integer, Long> toothCountFrequency = CollectionManager.getInstance().getDragons().values().stream()
                .filter(dragon -> dragon.getHead() != null)
                .collect(Collectors.groupingBy(dragon -> (int) dragon.getHead().getToothcount(), Collectors.counting()));

        // Filter dragons whose tooth count appears only once
        List<Dragon> uniqueDragons = CollectionManager.getInstance().getDragons().values().stream()
                .filter(dragon -> dragon.getHead() != null)
                .filter(dragon -> toothCountFrequency.getOrDefault(dragon.getHead().getToothcount(), 0L) == 1)
                .collect(Collectors.toList());

        // Return a response based on whether any unique dragons were found
        if (uniqueDragons.isEmpty()) {
            return new Response("No dragons with unique heads");
        }
        return new Response(String.valueOf(uniqueDragons));
    }
}

