package se.ifmo.client.chat;

import se.ifmo.client.commands.Command;
import se.ifmo.client.commands.util.HistoryManager;
import se.ifmo.client.console.Console;

import java.io.IOException;

/**
 * The {@link Handler} class is responsible for managing and executing commands.
 * It handles the processing of commands by adding the command to the history and then executing it.
 */
public class Handler {

    /**
     * Handles the execution of a given command. This method adds the command name to the history
     * and then executes the command with the provided request.
     *
     * @param command the command to execute
     * @param request the request containing the parameters for the command
     * @return a {@link Response} object containing the result of the command execution
     * @throws IOException if an I/O error occurs during the command execution
     */
    public static Response handleCommand(Command command, Request request) throws IOException {
        // Add the command to the history
        HistoryManager.getInstance().addCommand(command.getName());

        // Execute the command and return the response
        return command.execute(request);
    }
}
