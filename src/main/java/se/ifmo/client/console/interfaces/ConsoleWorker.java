package se.ifmo.client.console.interfaces;

/**
 * The {@link ConsoleWorker} interface defines methods for reading and writing data to and from the console.
 * It provides a generic interface that can be implemented to interact with different console-based input/output systems.
 * Classes implementing this interface are expected to define the specifics of how data is read and written.
 *
 * @param <T> the type of the data being read and written.
 */
public interface ConsoleWorker<T> extends AutoCloseable {

    /**
     * Reads data from the console or input source.
     *
     * @return the data read from the console.
     * @throws RuntimeException if there is an error during reading.
     */

    /**
     * Writes the specified value to the console or output destination.
     *
     * @param value the value to write to the console.
     */
    void write(T value);

    void writeln(T value);
}
