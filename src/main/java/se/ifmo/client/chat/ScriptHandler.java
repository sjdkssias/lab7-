package se.ifmo.client.chat;

import se.ifmo.client.commands.Command;
import se.ifmo.client.console.Console;
import se.ifmo.server.models.classes.Dragon;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * The {@link ScriptHandler} class is responsible for reading and executing a script file containing a series of commands.
 * Each line in the script file is treated as a command, which is processed and executed sequentially.
 * This class ensures that scripts are executed safely, with checks to prevent re-entrant execution of the same script.
 * The commands are processed by matching their name with available commands in a provided command map.
 */
public class ScriptHandler implements AutoCloseable {
    private static final HashSet<String> runningScripts = new HashSet<>();
    private final Path scriptPath;
    private final BufferedReader bufferedReader;
    private final Console console;
    private final Map<String, Command> commandMap;

    /**
     * Constructs a new {@link ScriptHandler} instance, initializing the script file, console, and command map.
     * It checks if the script file exists and ensures that the script is not already running.
     *
     * @param scriptPath The path to the script file to be executed.
     * @param console The console instance used for displaying messages.
     * @param commandMap A map of command names to {@link Command} objects used to handle the commands.
     * @throws IOException If an error occurs while reading the script file or if the script file does not exist.
     * @throws IllegalStateException If the script is already running.
     */
    public ScriptHandler(Path scriptPath, Console console, Map<String, Command> commandMap) throws IOException {
        this.console = console;
        this.scriptPath = scriptPath;
        this.commandMap = commandMap;

        // Check if the script file exists.
        if (Files.notExists(scriptPath)) {
            throw new FileNotFoundException("file " + scriptPath.getFileName() + " wasn't found.");
        }

        // Ensure the script is not already running.
        if (runningScripts.contains(scriptPath.getFileName().toString())) {
            throw new IllegalStateException("script  " + scriptPath.getFileName() + " is already running.");
        }

        // Initialize buffered reader for the script file.
        bufferedReader = new BufferedReader(new FileReader(scriptPath.toFile()));
        runningScripts.add(scriptPath.getFileName().toString());
    }

    /**
     * Runs the script by reading it line by line and executing each command.
     */
    public void run() {
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            console.write("Error of reading file: " + e.getMessage());
        } finally {
            close();
        }
    }

    /**
     * Processes a single line from the script.
     * This method splits the line into command and arguments, validates the command,
     * and then executes it by passing a {@link Request} to the appropriate {@link Command}.
     *
     * @param line The line from the script to process.
     */
    private void processLine(String line) {
        try {
            // Split the line into command and arguments.
            String[] parts = line.trim().split("\\s+", 2);
            String commandName = parts[0];
            List<String> args = (parts.length > 1) ? Arrays.asList(parts[1].split("\\s+")) : Collections.emptyList();

            // Get the command from the map.
            Command command = commandMap.get(commandName);
            if (command == null) {
                console.write("Unknown Command: " + commandName);
                return;
            }

            // Prepare a request with no dragons for simple commands.
            List<Dragon> dragons = new ArrayList<>();
            Request request = new Request(commandName, args, dragons, console);

            // Handle the command and display the result.
            Response response = Handler.handleCommand(command, request);
            console.write(response.getMessage());

        } catch (Exception e) {
            console.write("Error of processing command: " + e.getMessage());
        }
    }

    /**
     * Closes the buffered reader and removes the script from the list of running scripts.
     */
    @Override
    public void close() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            console.write("Error of closing file: " + e.getMessage());
        }
        runningScripts.remove(scriptPath.getFileName().toString());
    }
}
