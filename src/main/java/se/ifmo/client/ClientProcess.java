package se.ifmo.client;
import se.ifmo.client.chat.Response;
import se.ifmo.client.chat.Router;
import se.ifmo.client.console.Console;

import java.io.IOException;
import java.util.List;


public class ClientProcess {

    private Console console;
    private Client client;

    public ClientProcess(Console console, Client client){
        this.console = console;
        this.client = client;
    }

    protected void startProcess(){
        while (true) {
            try {
                String command = readCommandName();

                if (command.equalsIgnoreCase("exit")) {
                    console.writeln("Exiting");
                    break;
                }

                if (!validCommand(command)) {
                    continue;
                }

                String[] parts = command.split("\\s+", 2);
                String commandName = parts[0];
                String arguments = parts.length > 1 ? parts[1] : "";

                Response response = Router.routeCommand(commandName, List.of(arguments));

                handleResponse(response);

            } catch (IOException ioEx) {
                console.writeln("Connection error: " + ioEx.getMessage());
                try {
                    client.reconnect();
                } catch (IOException e) {
                    console.writeln("Failed to reconnect");
                    break;
                }
            } catch (Exception e) { // любые другие ошибки
                console.writeln("Unexpected error: " + e.getMessage());
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

    private String readCommandName(){
        return console.read().trim();
    }

    private boolean validCommand(String commandName){
        if (commandName == "save"){
            console.writeln("This command isn't allowed for client");
            return false;
        }
        return true;
    }

}
