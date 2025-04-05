package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.commands.util.HistoryManager;

/**
 * The {@link HistoryCommand} class represents a command that displays the history of previously executed commands.
 * This command allows users to view the list of commands that were executed previously.
 */
public class HistoryCommand extends Command {

    /**
     * Constructs a {@link HistoryCommand}.
     * Initializes the command with the name "history" and description "show history of commands".
     */
    public HistoryCommand() {
        super("history", "show history of commands");
    }

    /**
     * Executes the "history" command. This command retrieves and returns the history of all commands that have been
     * executed previously. The history is stored in a {@link HistoryManager} instance.
     *
     * @param request the request containing any arguments for the command execution (not used in this case)
     * @return a {@link Response} containing a formatted string of previously executed commands in the history
     */
    @Override
    public Response execute(Request request) {
        // Retrieve the history of commands and return it as a r
        return new Response(String.join("\n", HistoryManager.getInstance().getHistory()));
    }
}