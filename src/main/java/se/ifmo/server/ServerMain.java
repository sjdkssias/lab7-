package se.ifmo.server;

import se.ifmo.client.console.Console;

public class ServerMain {
    public static void main(String[] args) {
        try (Console console = new Console();
             Server server = new Server(console)) {

            while (true) {
                server.processKeys();
            }

        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}