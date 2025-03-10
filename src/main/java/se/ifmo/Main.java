package se.ifmo;

import se.ifmo.client.chat.Response;
import se.ifmo.client.chat.Router;
import se.ifmo.client.console.Console;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Console console = new Console();
        System.out.println("Введите команду (напишите 'help' для списка команд):");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");
            String commandName = parts[0];
            List<String> arguments = List.of(parts).subList(1, parts.length);
            Response response = Router.routeCommand(commandName, arguments, console);
            System.out.println(response.getMessage());
        }
    }
}