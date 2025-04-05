package se.ifmo.server.file.handlers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import lombok.*;

/**
 * Handles input and output operations with files and the console.
 * This class implements the {@link IOHandler} interface for reading and writing strings.
 * It provides methods to read from the console and write to a file, specifically "ldldl.txt".
 * Implements {@link AutoCloseable} to automatically close resources when done.
 */
public class FileHandler implements IOHandler<String>, AutoCloseable {

    /**
     * The {@link FileWriter} used for writing to the file "ldldl.txt".
     */
    @Getter
    protected final FileWriter writer;

    /**
     * The {@link InputStreamReader} used for reading input from the console.
     */
    @Getter
    protected final InputStreamReader reader;

    /**
     * Constructs a new {@link FileHandler} that initializes the file writer to append to "ldldl.txt"
     * and the input stream reader to read from the console.
     *
     * @throws IOException if there is an error opening the file for writing or reading input.
     */
    public FileHandler() throws IOException {
        this.writer = new FileWriter("ldldl.txt", true);  // Open file for appending
        this.reader = new InputStreamReader(System.in);   // Input stream from the console
    }

    /**
     * Reads a line of text from the console.
     *
     * @return the line of text entered by the user.
     * @throws IOException if an input or reading error occurs.
     */
    @Override
    public String read() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        System.out.println("Введите текст: ");
        return bufferedReader.readLine();
    }

    /**
     * Writes the provided string value to the file "ldldl.txt".
     * The text is appended to the file and the buffer is flushed immediately to ensure the data is written.
     *
     * @param value the string to be written to the file.
     * @throws IOException if an error occurs during writing.
     */
    @Override
    public void write(String value) throws IOException {
        writer.append(value + "\n").flush();  // Append the string to the file and flush the buffer
    }

    /**
     * Closes the resources used by the {@link FileHandler}, including the file writer and reader.
     *
     * @throws IOException if an error occurs while closing the resources.
     */
    @Override
    public void close() throws IOException {
        writer.close();  // Close the file writer
        reader.close();  // Close the input stream reader
    }
}

