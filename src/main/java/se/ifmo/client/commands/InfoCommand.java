package se.ifmo.client.commands;

import com.sun.source.tree.Tree;
import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.CollectionManager;
import se.ifmo.server.models.classes.Dragon;

import java.io.IOException;
import java.util.TreeMap;

public class InfoCommand extends Command{
    public InfoCommand() {
        super("info", "information about collection");
    }

    @Override
    public Response execute(Request request) throws IOException {
        TreeMap<Long, Dragon> collection = CollectionManager.getInstance().getDragons();
        return new Response("collection type: " + collection.getClass().getSimpleName() + "\n" + "collection size: " + collection.size());
    }
}
