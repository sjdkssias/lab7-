package se.ifmo.client.console;

import se.ifmo.client.console.interfaces.ConsoleWorker;

import java.io.*;

/**
 * The Console class is responsible for handling input and output operations to and from the system's console.
 * It implements the {@link ConsoleWorker} interface and provides methods for reading user input and writing output
 * to the console. It also manages resources related to input/output streams such as {@link BufferedReader} and
 * {@link BufferedWriter}.
 */
public final class Console implements ConsoleWorker<String> {
    private static Console instance;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));


    public static Console getInstance() {
        if (instance == null) {
            instance = new Console();
        }
        return instance;
    }
    /**
     * Reads a line of input from the console.
     *
     * @return the line of input entered by the user.
     * @throws RuntimeException if there is an error reading input from the console.
     */
    public String read() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении из консоли", e);
        }
    }

    /**
     * Writes a specified value to the console.
     * This method writes a string followed by a new line to the console and flushes the output stream.
     *
     * @param value the string to be written to the console.
     * @throws RuntimeException if there is an error writing to the console.
     */
    @Override
    public void write(String value) {
        try {
            writer.write(value);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error of writing to console", e);
        }
    }


    @Override
    public void writeln(String value) {
        try {
            writer.write(value);
            writer.newLine();
            writer.flush();
        } catch (IOException e){
            throw new RuntimeException("Error of writing to console");
        }
    }

    /**
     * Closes the input and output streams, releasing the resources.
     *
     * @throws IOException if there is an error while closing the streams.
     */
    @Override
    public void close() throws IOException {
        reader.close();
        writer.close();
    }
}
