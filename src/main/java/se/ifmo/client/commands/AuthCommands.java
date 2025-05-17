package se.ifmo.client.commands;

import java.util.List;

public class AuthCommands {

    public static final List<Command> AUTHCOMMANDS = List.of(
            new RegisterCommand(),
            new LoginCommand()
    );

    public AuthCommands() {
    }

}
