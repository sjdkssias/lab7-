package se.ifmo.client;
import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.chat.Router;
import se.ifmo.client.commands.Command;
import se.ifmo.client.console.Console;
import se.ifmo.client.utility.InputHandler;
import se.ifmo.server.models.classes.Dragon;
import java.io.IOException;
import java.util.List;

import static se.ifmo.client.commands.AllCommands.ALLCOMANDS;


public class ClientProcess {

    private Console console;
    private Client client;
    private Command command;
    public ClientProcess(Console console, Client client){
        this.console = console;
        this.client = client;
    }

    protected void startProcess(){
        while (true) {
            try {
                Request request = createRequest();

            } catch (Exception ioEx) {
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
    private void handleResponse(Response response) {
        if (response != null) {
            console.writeln("Server response: " + response.getMessage());
        } else {
            console.writeln("No response from the server.");
        }
    }
    private Request createRequest() throws InterruptedException {
        while (true) {
            String input = readCommandName();

            if (input.equalsIgnoreCase("exit")) {
                console.writeln("Exiting");
                break;
            }

            String[] parts = input.split("\\s+", 2);
            String commandName = parts[0];
            String arguments = parts.length > 1 ? parts[1] : "";
            List<Dragon> dragons = null;

            if (requiresDragons(commandName)) {
                dragons = List.of(InputHandler.get(console));
            }

            return new Request(commandName, List.of(arguments), dragons);
        }
    }

    private boolean requiresDragons(String commandName) {
        return ALLCOMANDS.stream()
                .anyMatch(temp -> temp.getName().equals(commandName)
                && temp.getElementNumber() != 0);
    }

    private String readCommandName(){
        return console.read().trim();
    }


}
