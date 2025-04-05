package se.ifmo.server.file.handlers;

import java.io.IOException;

/**
 * Interface for handling input and output operations with generic data types.
 * This interface defines methods for reading and writing data, with the
 * specific data type being determined by the implementing class.
 * It extends {@link AutoCloseable}, allowing implementations to automatically
 * close resources when done.
 *
 * @param <T> the type of data that will be read or written.
 */
public interface IOHandler<T> extends AutoCloseable {

    /**
     * Reads data from an input source.
     *
     * @return the data read, of type {@link T}.
     * @throws IOException if an I/O error occurs while reading.
     */
    T read() throws IOException;

    /**
     * Writes data to an output source.
     *
     * @param value the data of type {@link T} to be written.
     * @throws IOException if an I/O error occurs while writing.
     */
    void write(T value) throws IOException;
}
