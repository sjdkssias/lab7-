package se.ifmo.client.chat;

import se.ifmo.client.commands.AllCommands;
import se.ifmo.client.commands.Command;
import se.ifmo.client.console.Console;
import se.ifmo.client.utility.InputHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Router} class is responsible for routing commands to their respective handlers.
 * It matches the input command name to a registered command, creates a request object,
 * and passes it to the command handler for execution.
 */
public class Router {

    /**
     * Routes a command to the appropriate handler based on the command name.
     * If the command is found, a {@link Request} is created with the necessary arguments
     * and passed to the command for execution. If the command is not found, an error response is returned.
     *
     * @param commandName The name of the command to be executed.
     * @param arguments The list of arguments to be passed to the command.
     * @return The response from the command execution, typically a message or result of the command.
     * @throws IOException If an error occurs during the execution of the command.
     */
    public static Response routeCommand(String commandName, List<String> arguments) throws IOException {
        // Iterate through all available commands to find a match.
        Console console = Console.getInstance();
        for (Command command : AllCommands.ALLCOMANDS) {
            // Check if the command name matches, ignoring case.
            if (command.getName().equalsIgnoreCase(commandName)) {
                Request request;

                // If the command does not require any elements (arguments), create a request without them.
                if (command.getElementNumber() == 0) {
                    request = new Request(commandName, arguments, new ArrayList<>());
                } else {
                    // Otherwise, attempt to fetch the necessary element (e.g., a dragon) using InputHandler.
                    try {
                        request = new Request(commandName, arguments, List.of(InputHandler.get(console)));
                    } catch (InterruptedException e) {
                        // If interrupted, create a request with no elements.
                        request = new Request(commandName, arguments, new ArrayList<>());
                    }
                }
                // Handle the command and return the response.
                return Handler.handleCommand(command, request);
            }
        }

        // If no matching command is found, return an error message.
        return new Response("Error: command '" + commandName + "' wasn't found");
    }
}

