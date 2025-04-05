package se.ifmo.client.commands;

import java.util.List;

/**
 * The {@link AllCommands} class holds a static list of all available commands in the application.
 * It is used to manage and access all the commands that can be executed by the client.
 *
 * <p>This class serves as a centralized collection of command objects, which can be used to execute
 * various actions like adding dragons, showing information, and more.</p>
 */
public class AllCommands {

    /**
     * A static list containing all available commands.
     * Each command in this list is an instance of a specific subclass of {@link Command}.
     * The list is initialized with the full set of commands the client can execute.
     */
    public static final List<Command> ALLCOMANDS = List.of(
            new AddCommand(),
            new HelpCommand(),
            new ExitCommand(),
            new FilterGreaterThanHeadCommand(),
            new HistoryCommand(),
            new InsertCommand(),
            new SaveCommand(),
            new ClearCommand(),
            new MaxByIdCommand(),
            new RemoveKeyCommand(),
            new RemoveGreaterKeyCommand(),
            new PrintUniqueHeadCommand(),
            new RemoveLowerKeyCommand(),
            new ShowCommand(),
            new ExecuteScriptCommand(),
            new InfoCommand()
    );

    /**
     * Constructs an {@link AllCommands} object. This constructor is used to initialize the class,
     * although the static list of commands is already initialized and does not require instantiation.
     */
    public AllCommands() {
    }
}

