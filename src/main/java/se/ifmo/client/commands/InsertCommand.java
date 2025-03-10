package se.ifmo.client.commands;
import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.console.Console;
import se.ifmo.server.CollectionManager;

import java.io.IOException;


public class InsertCommand extends Command{

    public InsertCommand() {
        super("insert", "add element with your key", 1);
    }

    @Override
    public Response execute(Request request) throws IOException {
        if (request.dragons() == null || request.dragons().isEmpty()) {
            return new Response("No dragons to add");
        }

        Console console = new Console();

        console.write("Enter the ID for the new dragon (Integer value):");
        String idString = console.read();
        Long id;
        try {
            id = Long.parseLong(idString);
        } catch (NumberFormatException e) {
            return new Response("Invalid ID format. Please enter a valid integer.");
        }
        if (CollectionManager.getInstance().containsId(id)) {
            return new Response("Dragon with this ID already exists!");
        }
        CollectionManager.getInstance().getDragons().put(id, request.dragons().get(0));

        return new Response("Dragon was saved to collection with ID: " + id);
    }
}
