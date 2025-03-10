package se.ifmo.client.commands;

import se.ifmo.client.console.Console;

import java.util.List;

public class AllCommands {
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

    public AllCommands() {
    }
}
