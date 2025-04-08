package se.ifmo.client;

import se.ifmo.client.chat.Request;
import se.ifmo.client.commands.ExecuteScriptCommand;
import se.ifmo.client.console.Console;
import se.ifmo.client.utility.InputHandler;
import se.ifmo.server.models.classes.Dragon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static se.ifmo.client.commands.AllCommands.ALLCOMANDS;

/**
 * Handles client-side command processing and script execution.
 * Manages the main interaction loop between the client and server,
 * including command parsing, request creation, and script handling.
 */
public class ClientProcess {

    /** Console interface for user I/O operations. */
    private Console console;

    /** Client instance for server communication. */
    private Client client;

    /**
     * Constructs a ClientProcess with the specified console and client.
     *
     * @param console the console used for input/output operations
     * @param client the client instance handling server communication
     */
    public ClientProcess(Console console, Client client) {
        this.console = console;
        this.client = client;
    }

    /**
     * Starts the main processing loop for handling user commands.
     * Continuously reads input, processes commands, and manages server communication.
     * Handles script execution for commands starting with "execute_script".
     * Automatically attempts reconnection on communication failures.
     */
    protected void startProcess() {
        while (true) {
            try {
                String inputLine = readCommandWithArgs();
                if (inputLine.startsWith((new ExecuteScriptCommand()).getName())) {
                    (new ScriptHandler()).handleInput(inputLine);
                }
                client.sendRequest(createRequest(inputLine));
                client.receiveResponse();

            } catch (IOException ioEx) {
                console.writeln("Connection error: " + ioEx.getMessage());
                try {
                    client.reconnect();
                } catch (IOException e) {
                    console.writeln("Failed to reconnect");
                    break;
                }
            }
        }
    }

    /**
     * Creates a Request object from user input.
     * Handles special "exit" command to terminate the application.
     * For commands requiring Dragon objects, prompts user for additional input.
     *
     * @param input the raw command input string
     * @return Request object containing command details, or null if interrupted
     */
    public Request createRequest(String input) {
        if (input.equalsIgnoreCase("exit")) {
            console.writeln("Exiting");
            System.exit(0);
        }
        String[] parts = input.split("\\s+", 2);
        String commandName = parts[0];
        String arguments = parts.length > 1 ? parts[1] : "";
        List<Dragon> dragons = null;

        try {
            if (requiresDragons(commandName)) dragons = List.of(InputHandler.get(console));
        } catch (InterruptedException e) {
            return null;
        }

        return new Request(commandName, List.of(arguments), dragons);
    }

    /**
     * Checks if a command requires Dragon objects as additional input.
     *
     * @param commandName the name of the command to check
     * @return true if the command requires Dragon objects, false otherwise
     */
    private boolean requiresDragons(String commandName) {
        return ALLCOMANDS.stream()
                .anyMatch(temp -> temp.getName().equals(commandName)
                        && temp.getElementNumber() != 0);
    }

    /**
     * Reads a command with arguments from the console.
     *
     * @return trimmed input string from the console
     */
    private String readCommandWithArgs() {
        return console.read().trim();
    }

    /**
     * Handles execution of script files containing multiple commands.
     * Processes each non-empty line in the script file as a separate command.
     */
    class ScriptHandler {
        private final Set<String> executingScripts = new HashSet<>();
        private final int maxRecursionDepth = 20;
        private int currentDepth = 0;

        /**
         * Processes script file execution.
         *
         * @param input the full input string containing the script command and path
         */
        private void handleInput(String input) {
            String[] parts = input.split("\\s+", 2);
            if (parts.length < 2) {
                console.writeln("No script file specified.");
                return;
            }
            String scriptPath = parts[1];
            if (currentDepth >= maxRecursionDepth) {
                console.writeln("Maximum recursion depth (" + maxRecursionDepth + ") exceeded.");
                return;
            }
            executingScripts.add(scriptPath);
            currentDepth++;
            try (BufferedReader reader = new BufferedReader(new FileReader(scriptPath))) {
                reader.lines()
                        .filter(line -> !line.trim().isEmpty())
                        .forEach(line -> {
                            try {
                                client.sendRequest(createRequest(line));
                                client.receiveResponse();
                            } catch (IOException e) {
                                console.writeln("Error handling line: " + e.getMessage());
                            }
                        });

            } catch (IOException e) {
                console.writeln("Error reading script file: " + e.getMessage());
            }
        }
    }
}