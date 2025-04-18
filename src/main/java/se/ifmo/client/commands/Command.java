package se.ifmo.client.commands;

import lombok.*;
import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;

import java.io.IOException;

/**
 * The {@link Command} class represents a base class for commands in the client application.
 * Each commandName has a name, description, and an element number (optional).
 * The class is designed to be extended by specific commandName implementations, where the execution logic is defined.
 *
 * <p>The {@link Command} class provides an abstract method {@link #execute(Request)} to be implemented
 * by subclasses to execute the commandName logic.</p>
 */
@Getter
@RequiredArgsConstructor
public abstract class Command {
    private final String name;
    private final String description;
    private final int elementNumber;

    /**
     * Constructs a {@link Command} with the specified name, description, and an optional element number.
     *
     * @param name the name of the commandName.
     * @param description a brief description of what the commandName does.
     */
    public Command(String name, String description) {
        this(name, description, 0);
    }

    /**
     * Executes the commandName logic, which is expected to be defined by subclasses.
     * This method takes a {@link Request} object as input and returns a {@link Response} object as output.
     *
     * @param request the request object containing necessary data for executing the commandName.
     * @return the response object containing the result of the commandName execution.
     * @throws IOException if an I/O error occurs during the execution of the commandName.
     */
    public abstract Response execute(Request request);
}
