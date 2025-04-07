package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;

/**
 * The {@link ExitCommand} class represents a commandName that allows the user to exit the program.
 * This commandName terminates the application when executed.
 */
public class ExitCommand extends Command {

    /**
     * Constructs an {@link ExitCommand}.
     * The constructor initializes the commandName with the name "exit" and the description "exit program".
     */
    public ExitCommand() {
        super("exit", "exit program");
    }

    /**
     * Executes the "exit" commandName, which terminates the application.
     * A message indicating the program is exiting is returned in the response before the program is terminated.
     *
     * @param request the request containing the arguments (if any) for the commandName execution
     * @return a {@link Response} containing the message that the program is exiting
     */
    @Override
    public Response execute(Request request) {
        System.exit(0);
        return new Response("Program's exiting..........");
    }
}
