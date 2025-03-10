package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.CollectionManager;
import se.ifmo.server.models.classes.Dragon;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class RemoveLowerKeyCommand extends Command{
    public RemoveLowerKeyCommand() {
        super("remove_lower_key", "remove from the collection all elements whose key is less than the given one", 0);
    }

    @Override
    public Response execute(Request request) {
        if (request.args() == null) {
            return new Response("null request");
        }
        long yourKey = Integer.parseInt(request.args().get(0));

        TreeMap<Long, Dragon> collection = CollectionManager.getInstance().getDragons();
        List<Long> keysToRemove = new ArrayList<>(collection.headMap(yourKey, false).keySet());
        if (keysToRemove.isEmpty()) {
            return new Response("No elements found with key lower than " + yourKey);
        }
        for (long key : keysToRemove) {
            collection.remove(key);
        }

        return new Response("Removed " + keysToRemove.size() + " elements with key lower than " + yourKey);
    }

}
