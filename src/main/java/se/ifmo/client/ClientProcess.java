package se.ifmo.client;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.chat.UserRec;
import se.ifmo.client.commands.ExecuteScriptCommand;
import se.ifmo.client.commands.LoginCommand;
import se.ifmo.client.commands.LogoutCommand;
import se.ifmo.client.commands.RegisterCommand;
import se.ifmo.client.console.Console;
import se.ifmo.client.utility.InputHandler;
import se.ifmo.server.Server;
import se.ifmo.server.models.classes.Dragon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static se.ifmo.client.commands.AllCommands.ALLCOMANDS;

/**
 * Handles client-side command processing and script execution.
 * Manages the main interaction loop between the client and server,
 * including command parsing, request creation, and script handling.
 */
public class ClientProcess {

    private Client client;
    private UserRec currentUser;
    private Console console;
    /**

     * @param client the client instance handling server communication
     */
    public ClientProcess(Client client, Console console) {
        this.client = client;
        this.console = console;
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

                Request req = createRequest(inputLine);

                if (req == null) {
                    continue;
                }
                client.sendRequest(req);
                Response resp = client.receiveResponse();
                if ((req.commandName().equals(new LoginCommand().getName()))) {
                    if (resp.success()) {
                        this.currentUser = req.userRec();
                    }
                }
                if ((req.commandName().equals(new LogoutCommand().getName()))){
                    this.currentUser = null;
                }

            } catch (IOException ioEx) {
                try {
                    client.reconnect();
                } catch (IOException e) {
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
            System.exit(0);
        }

        String[] parts = input.split("\\s+", 3);
        String commandName = parts[0];
        String arguments = parts.length > 1 ? parts[1] : "";

        List<Dragon> dragons = null;
        if (commandName.equals(new RegisterCommand().getName()) || commandName.equals(new LoginCommand().getName())) {
            if (parts.length < 3) {
                return null;
            }
            return new Request(commandName, List.of(parts[1], parts[2]), null, new UserRec(parts[1], parts[2]));
        }

        try {
            if (currentUser == null) {
                return null;
            }
            if (requiresDragons(commandName)) dragons = List.of(InputHandler.get(new Console(), currentUser));
        } catch (InterruptedException e) {
            Server.logger.error("Command Interrupt");
            return null;
        }
        return new Request(commandName, List.of(arguments), dragons, currentUser);
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
        return console.read();
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
                return;
            }
            String scriptPath = parts[1];
            if (currentDepth >= maxRecursionDepth) {
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
                            }
                        });

            } catch (IOException e) {
            }
        }
    }
}