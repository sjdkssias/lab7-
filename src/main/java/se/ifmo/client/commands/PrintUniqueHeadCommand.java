package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.collectionManagement.CollectionManager;
import se.ifmo.server.models.classes.Dragon;

import java.util.ArrayList;
import java.util.Collection;
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

        Collection<Dragon> dragonsValues;

        dragonsValues = new ArrayList<>(CollectionManager.getInstance().getDragons().values());
        Map<Integer, Long> toothCountFrequency = dragonsValues.stream() 
                .filter(dragon -> dragon.getHead() != null)
                .collect(Collectors.groupingBy(dragon -> (int) dragon.getHead().getToothcount(), Collectors.counting()));

        List<Dragon> uniqueDragons = dragonsValues.stream()
                .filter(dragon -> dragon.getHead() != null)
                .filter(dragon -> toothCountFrequency.getOrDefault((int) dragon.getHead().getToothcount(), 0L) == 1)
                .collect(Collectors.toList());

        if (uniqueDragons.isEmpty()) {
            return new Response(false,"No dragons with unique heads");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Dragon dragon : uniqueDragons) {
                sb.append(dragon.toString()).append("\n");
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
            return new Response(sb.toString());
        }
    }
}

