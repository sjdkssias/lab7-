package se.ifmo.client.utility;

import se.ifmo.client.console.Console;
import se.ifmo.server.models.classes.Coordinates;
import se.ifmo.server.models.classes.Dragon;
import se.ifmo.server.models.classes.DragonHead;
import se.ifmo.server.models.enums.Color;
import se.ifmo.server.models.enums.DragonCharacter;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Utility class that provides functionality for interacting with the user to input values for a {@link Dragon}.
 * It handles collecting input for various attributes of a dragon, including its name, coordinates, speaking status,
 * color, character, and head attributes. The input is validated and parsed before being applied to the dragon object.
 */
public class InputHandler {

    /**
     * Collects the necessary input from the user to create a {@link Dragon} object.
     * This method repeatedly prompts the user for valid inputs until all required fields are populated.
     *
     * @param console the {@link Console} object used to read user input and display prompts.
     * @return the created {@link Dragon} object with the user-provided attributes.
     * @throws InterruptedException if the operation is interrupted (e.g., user cancels input).
     */
    public static Dragon get(Console console) throws InterruptedException {
        Dragon dragon = new Dragon();

        // Collect name input
        while (!input("name", dragon::setName, Function.identity(), console)) {}

        // Collect coordinates input
        Coordinates coordinates = new Coordinates();
        while (!input("coordinate x", coordinates::setX, Float::parseFloat, console)) {};
        while (!input("coordinate y", coordinates::setY, Long::valueOf, console)) {};
        dragon.setCoordinates(coordinates);

        // Collect speaking status input
        while (!input("speaking", dragon::setSpeaking, value -> {
            if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                throw new IllegalArgumentException("Invalid input! Please enter 'true' or 'false'.");
            }
            return Boolean.valueOf(value);
        }, console));

        // Collect color input
        while (!input("color", dragon::setColor, Color::valueOf, console)) {};

        // Collect character input
        while (!input("character", dragon::setCharacter, DragonCharacter::valueOf, console)) {};

        // Collect dragon head input
        DragonHead head = new DragonHead();
        while (!input("dragon head", head::setToothcount, Float::parseFloat, console));
        dragon.setHead(head);

        return dragon;
    }

    /**
     * Generic method to handle input collection for a given field of the {@link Dragon}.
     * It prompts the user, parses the input, and sets the corresponding field using the provided setter method.
     *
     * @param <K> the type of the field value.
     * @param fieldName the name of the field being requested.
     * @param setter the setter method for applying the parsed value to the dragon object.
     * @param parser the function to parse the user input into the appropriate type.
     * @param console the {@link Console} object for input/output operations.
     * @return true if valid input was provided, false otherwise.
     * @throws InterruptedException if the operation is interrupted by the user.
     */
    private static <K> boolean input(
            final String fieldName,
            final Consumer<K> setter,
            final Function<String, K> parser,
            final Console console
    ) throws InterruptedException {
        try {
            console.write(" - " + fieldName + ": ");
            String line = console.read();
            if (line == null || line.equals("return")) throw new InterruptedException("called return");

            if (line.isBlank()) setter.accept(null);
            else setter.accept(parser.apply(line));

            return true;
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception ex) {
            console.write(ex.getMessage() + System.lineSeparator());
            return false;
        }
    }
}
