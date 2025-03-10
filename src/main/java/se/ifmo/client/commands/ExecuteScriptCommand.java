package se.ifmo.client.commands;


import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.chat.ScriptHandler;
import se.ifmo.client.console.Console;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ExecuteScriptCommand extends Command {
    public ExecuteScriptCommand() {
        super("execute_script", "Считать и исполнить скрипт из указанного файла");
    }

    @Override
    public Response execute(Request request) {
        if (request.args().isEmpty()) {
            return new Response("Ошибка: укажите путь к файлу скрипта.");
        }

        Console console = CommandContext.getConsole();
        Map<String, Command> commandMap = CommandContext.getCommandMap();

        Path scriptPath = Paths.get(request.args().get(0));
        try (ScriptHandler scriptHandler = new ScriptHandler(scriptPath, console, commandMap)) {
            scriptHandler.run();
            return new Response("Скрипт выполнен успешно.");
        } catch (FileNotFoundException e) {
            return new Response("Ошибка: файл не найден.");
        } catch (IOException e) {
            return new Response("Ошибка чтения файла: " + e.getMessage());
        } catch (IllegalStateException e) {
            return new Response("Ошибка: зацикливание вызова скриптов.");
        }
    }
}