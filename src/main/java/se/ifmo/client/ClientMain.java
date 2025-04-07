package se.ifmo.client;

import se.ifmo.client.console.Console;

public class ClientMain {
    public static void main(String[] args) {
        try (Console console = new Console();
             Client client = new Client(console)) {
            client.start();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

