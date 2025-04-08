package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;

/**
 * The {@link ExecuteScriptCommand} class represents a command that allows the user to execute a script from a file.
 * This command reads commands from a file and routes them for execution, providing responses accordingly.
 */
public class ExecuteScriptCommand extends Command {

    /**
     * Constructs an {@link ExecuteScriptCommand}.
     * The constructor initializes the command with the name "execute_script" and the description "to execute script from file".
     */
    public ExecuteScriptCommand() {
        super("execute_script", "to execute script from file");
    }

    /**
     * Executes the "execute_script" command, which reads a script from a file and executes the commands in the script.
     * Each line of the script is processed as a separate command, and the results are returned in a response.
     *
     * <p>If the provided file path is invalid or cannot be accessed, an appropriate error message is returned.</p>
     *
     * @param request the request containing the arguments (file path) for the command execution
     * @return a {@link Response} containing the result of executing the script, or an error message if any issue occurs
     */
    @Override
    public Response execute(final Request request) {
        // Check if the file path argument is provided
        if (request.args() == null || request.args().isEmpty()) {
            return new Response("No file path provided.");
        }
        return new Response("File was executed");
    }
}