package se.ifmo.client.chat;

import se.ifmo.client.commands.AllCommands;
import se.ifmo.client.commands.AuthCommands;
import se.ifmo.client.commands.util.HistoryManager;


/**
 * The {@link Router} class is responsible for routing commands to their respective handlers.
 * It matches the input commandName name to a registered commandName, creates a request object,
 * and passes it to the commandName handler for execution.
 */
public class Router {

    public static Response route(Request req){
        boolean isAuthCommand = AuthCommands.AUTHCOMMANDS.stream()
                .anyMatch(authCmd -> authCmd.getName().equalsIgnoreCase(req.commandName()));
        if (!isAuthCommand && req.userRec() == null){
            return new Response("You must to register or login.");
        }
        return AllCommands.ALLCOMANDS.stream()
                .filter(command -> command.getName().equals(req.commandName()))
                .findFirst().map(command -> {
                    HistoryManager.getInstance().addCommand(command.getName());
                    System.out.println(req);
                    return command.execute(req);
                }).orElse(new Response("No such command"));
    }

}

