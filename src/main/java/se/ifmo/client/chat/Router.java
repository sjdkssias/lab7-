package se.ifmo.client.chat;

import se.ifmo.client.commands.AllCommands;


/**
 * The {@link Router} class is responsible for routing commands to their respective handlers.
 * It matches the input commandName name to a registered commandName, creates a request object,
 * and passes it to the commandName handler for execution.
 */
public class Router {

    public static Response route(Request req){
        return AllCommands.ALLCOMANDS.stream()
                .filter(command -> command.getName().equals(req.commandName()))
                .findFirst().map(command -> command.execute(req)).orElse(new Response("No such command"));
    }
}

