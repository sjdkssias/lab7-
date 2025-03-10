package se.ifmo.client.commands;


import se.ifmo.client.console.Console;

import java.util.Map;

public class CommandContext {
    private static Console console;
    private static Map<String, Command> commandMap;

    // Private constructor to prevent instantiation
    private CommandContext() {}

    // Set the Console instance
    public static void setConsole(Console console) {
        CommandContext.console = console;
    }

    // Set the command map
    public static void setCommandMap(Map<String, Command> commandMap) {
        CommandContext.commandMap = commandMap;
    }

    // Get Console instance
    public static Console getConsole() {
        return console;
    }

    // Get command map
    public static Map<String, Command> getCommandMap() {
        return commandMap;
    }
}


