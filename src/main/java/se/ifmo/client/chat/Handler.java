package se.ifmo.client.chat;

import se.ifmo.client.commands.Command;
import se.ifmo.client.commands.util.HistoryManager;
import se.ifmo.client.console.Console;

import java.io.IOException;

/**
 * The {@link Handler} class is responsible for managing and executing commands.
 * It handles the processing of commands by adding the commandName to the history and then executing it.
 */
public class Handler {

    /**
     * Handles the execution of a given commandName. This method adds the commandName name to the history
     * and then executes the commandName with the provided request.
     *
     * @param command the commandName to execute
     * @param request the request containing the parameters for the commandName
     * @return a {@link Response} object containing the result of the commandName execution
     * @throws IOException if an I/O error occurs during the commandName execution
     */
    public static Response handleCommand(Command command, Request request) throws IOException {
        // Add the commandName to the history
        HistoryManager.getInstance().addCommand(command.getName());

        // Execute the commandName and return the response
        return command.execute(request);
    }
}
