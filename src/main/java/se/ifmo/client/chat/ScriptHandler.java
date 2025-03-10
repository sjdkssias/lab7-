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

import java.util.HashSet;

public class ScriptHandler implements AutoCloseable {
    private static final HashSet<String> runningScripts = new HashSet<>();
    private final Path scriptPath;
    private final BufferedReader bufferedReader;
    private final Console console;
    private final Map<String, Command> commandMap;

    public ScriptHandler(Path scriptPath, Console console, Map<String, Command> commandMap) throws IOException {
        this.console = console;
        this.scriptPath = scriptPath;
        this.commandMap = commandMap;

        if (Files.notExists(scriptPath)) {
            throw new FileNotFoundException("Файл " + scriptPath.getFileName() + " не найден.");
        }

        if (runningScripts.contains(scriptPath.getFileName().toString())) {
            throw new IllegalStateException("Скрипт " + scriptPath.getFileName() + " уже выполняется.");
        }

        bufferedReader = new BufferedReader(new FileReader(scriptPath.toFile()));
        runningScripts.add(scriptPath.getFileName().toString());
    }

    public void run() {
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            console.write("Ошибка чтения файла: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void processLine(String line) {
        try {
            String[] parts = line.trim().split("\\s+", 2);
            String commandName = parts[0];
            List<String> args = (parts.length > 1) ? Arrays.asList(parts[1].split("\\s+")) : Collections.emptyList();

            Command command = commandMap.get(commandName);
            if (command == null) {
                console.write("Неизвестная команда: " + commandName);
                return;
            }

            List<Dragon> dragons = new ArrayList<>(); // Заглушка, добавьте нужную логику
            Request request = new Request(commandName, args, dragons, console);
            Response response = Handler.handleCommand(command, request);
            console.write(response.getMessage());

        } catch (Exception e) {
            console.write("Ошибка обработки команды: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            console.write("Ошибка закрытия файла: " + e.getMessage());
        }
        runningScripts.remove(scriptPath.getFileName().toString());
    }

}