package se.ifmo.client;

import se.ifmo.client.chat.Request;
import se.ifmo.client.console.Console;
import se.ifmo.client.utility.InputHandler;
import se.ifmo.server.models.classes.Dragon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static se.ifmo.client.commands.AllCommands.ALLCOMANDS;


public class ClientProcess {

    private Console console;
    private Client client;

    public ClientProcess(Console console, Client client) {
        this.console = console;
        this.client = client;
    }

    protected void startProcess() {
        while (true) {
            try {
                if (readCommandName().toLowerCase().startsWith("execute_script")) {
                    executeScript(readCommandName());
                }
                Request request = createRequest(readCommandName());

                client.sendRequest(request);
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
    private void executeScript(String input) {
        String[] parts = input.split("\\s+", 2);
        if (parts.length < 2) {
            console.writeln("No script file specified.");
            return;
        }
        String scriptPath = parts[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(scriptPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Request request = createRequest(line);
                client.sendRequest(request);
                client.receiveResponse();
            }
        } catch (IOException e) {
            console.writeln("Error reading script file: " + e.getMessage());
        }
    }
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


    private boolean requiresDragons(String commandName) {
        return ALLCOMANDS.stream()
                .anyMatch(temp -> temp.getName().equals(commandName)
                        && temp.getElementNumber() != 0);
    }

    private String readCommandName() {
        return console.read().trim();
    }

}
