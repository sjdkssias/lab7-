package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.chat.Router;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        final Path path = Paths.get(request.args().get(0));

        // Validate the file path
        if (!path.toFile().exists()) return new Response("File not found.");
        if (!path.toFile().isFile()) return new Response("Path is not a file.");
        if (!path.toFile().canRead()) return new Response("Not enough rights to read file.");

        // Read and execute the script from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            StringBuilder result = new StringBuilder();

            // Process each line of the file
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Response cr = Router.routeCommand(line, null);
                    result.append(cr.getMessage()).append(System.lineSeparator());
                }
            }
            return new Response(result.toString());
        } catch (Exception e) {
            return new Response("Error occurred: %s".formatted(e.getMessage()));
        }
    }
}
