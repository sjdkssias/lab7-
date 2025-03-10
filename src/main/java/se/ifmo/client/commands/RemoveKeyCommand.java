package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.CollectionManager;

public class RemoveKeyCommand extends Command{
    public RemoveKeyCommand() {
        super("remove_key", "remove all elements who have the given key", 0);
    }

    @Override
    public Response execute(Request request) {
        if (request.args() == null){
            return new Response("null request");
        }
        long yourKey = Long.parseLong(request.args().get(0));
        CollectionManager.getInstance().removeById(yourKey);
        return new Response("elements with key " + yourKey+ " were removed");
    }
}
