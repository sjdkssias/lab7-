package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.CollectionManager;
import se.ifmo.server.models.classes.Dragon;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class RemoveGreaterKeyCommand extends Command{

    public RemoveGreaterKeyCommand() {
        super("remove_greater_key", "remove all elements whose key greater than your", 0);
    }

    @Override
    public Response execute(Request request) {
        System.out.println(request);
        if (request.args() == null) {
            return new Response("null request");
        }

        long yourKey = Long.parseLong(request.args().get(0));

        TreeMap<Long, Dragon> collection = CollectionManager.getInstance().getDragons();
        List<Long> keysToRemove = new ArrayList<>(collection.tailMap(yourKey, false).keySet());
        if (keysToRemove.isEmpty()) {
            return new Response("No elements found with key lower than " + yourKey);
        }

        for (long key : keysToRemove) {
            collection.remove(key);
        }

        return new Response("Removed " + keysToRemove.size() + " elements with key greater than " + yourKey);
    }
}
