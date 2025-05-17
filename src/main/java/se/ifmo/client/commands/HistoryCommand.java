package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.commands.util.HistoryManager;

/**
 * The {@link HistoryCommand} class represents a commandName that displays the history of previously executed commands.
 * This commandName allows users to view the list of commands that were executed previously.
 */
public class HistoryCommand extends Command {

    /**
     * Constructs a {@link HistoryCommand}.
     * Initializes the commandName with the name "history" and description "show history of commands".
     */
    public HistoryCommand() {
        super("history", "show history of commands");
    }

    /**
     * Executes the "history" commandName. This commandName retrieves and returns the history of all commands that have been
     * executed previously. The history is stored in a {@link HistoryManager} instance.
     *
     * @param request the request containing any arguments for the commandName execution (not used in this case)
     * @return a {@link Response} containing a formatted string of previously executed commands in the history
     */
    @Override
    public Response execute(Request request) {

        return new Response(String.join("\n", HistoryManager.getInstance().getHistory()));
    }
}