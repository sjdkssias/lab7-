package se.ifmo.client.commands.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * The {@link CommandsBuff} class is used to manage a fixed-size buffer of commands.
 * It keeps track of the most recent commands, ensuring that if the buffer reaches its maximum size,
 * the oldest commandName is discarded in favor of the new one.
 */
class CommandsBuff {
    private final Deque<String> deque;
    private final int size;

    /**
     * Constructs a {@link CommandsBuff} with the specified size.
     *
     * @param size the maximum number of commands to store in the buffer.
     */
    public CommandsBuff(int size) {
        this.deque = new ArrayDeque<>(size);
        this.size = size;
    }

    /**
     * Adds a new commandName to the buffer. If the buffer is full, the oldest commandName is removed to make room
     * for the new one.
     *
     * @param element the commandName to add to the buffer.
     */
    public void add(String element) {
        if (deque.size() == size) deque.removeFirst();
        deque.addLast(element);
    }

    /**
     * Converts the commands buffer to a list of commands.
     *
     * @return a {@link List} containing the commands in the buffer, in their current order.
     */
    public List<String> toList() {
        return deque.stream().toList();
    }
}

/**
 * The {@link HistoryManager} class manages a history of commands by keeping track of the most recent commands
 * in a fixed-size buffer. It provides methods to add new commands and retrieve the history of commands.
 */
public class HistoryManager {
    private static HistoryManager instance;
    private final CommandsBuff commandsBuff = new CommandsBuff(6);

    /**
     * Constructs a {@link HistoryManager} instance. The constructor is private to implement the singleton pattern.
     */
    private HistoryManager() {}

    /**
     * Gets the singleton instance of the {@link HistoryManager}.
     *
     * @return the singleton instance of the {@link HistoryManager}.
     */
    public static HistoryManager getInstance() {
        return instance == null ? instance = new HistoryManager() : instance;
    }

    /**
     * Adds a new commandName to the commandName history.
     *
     * @param command the commandName to add to the history.
     */
    public void addCommand(String command) {
        commandsBuff.add(command);
    }

    /**
     * Retrieves the list of commands in the history.
     *
     * @return a {@link List} of the most recent commands in the history.
     */
    public List<String> getHistory() {
        return commandsBuff.toList();
    }
}

