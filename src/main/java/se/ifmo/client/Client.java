package se.ifmo.client;

import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.console.Console;

import java.io.*;
import java.net.*;

/**
 * A client class for socket-based communication with a server.
 * Implements {@link AutoCloseable} to ensure proper resource cleanup.
 */
public class Client implements AutoCloseable {

    /** Default server hostname. */
    private static final String HOST = "192.168.10.80";
    /** Default server port. */
    private static final int PORT = 8345;

    /** Socket connection to the server. */
    private Socket socket;
    /** Console interface for user I/O operations. */
    private Console console;
    /** Input stream for receiving server responses. */
    private InputStream in;
    /** Connection status flag. */
    private boolean isConnected;
    /** Output stream for sending requests to the server. */
    private OutputStream out;

    /**
     * Constructs a new client instance with the specified console.
     *
     * @param console the console used for input/output operations.
     */
    public Client(Console console) {
        this.console = console;
        init();
    }

    /**
     * Initializes the connection to the server.
     * Sets up the socket, input, and output streams.
     */
    public void init() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(HOST, PORT));
            out = socket.getOutputStream();
            in = socket.getInputStream();
            isConnected = true;
            console.writeln("Successfully connected");
        } catch (UnknownHostException e) {
            console.writeln("Unknown host to connect: " + e.getMessage());
            isConnected = false;
        } catch (IOException e) {
            isConnected = false;
        }
    }

    /**
     * Starts the client process.
     * Attempts reconnection if no active connection exists.
     *
     * @throws IOException if an I/O error occurs.
     * @throws InterruptedException if the thread is interrupted during reconnection delay.
     */
    public void start() throws IOException, InterruptedException {
        while (!isConnected) {
            console.writeln("Connecting to the server...");
            reconnect();
            Thread.sleep(1000);
        }
        (new ClientProcess(console, this)).startProcess();
    }

    /**
     * Sends a serialized request to the server.
     *
     * @param request the request object to be sent.
     * @throws IOException if the connection is broken or I/O fails.
     */
    protected void sendRequest(Request request) throws IOException {
        if (!isConnected) {
            reconnect();
        }
        out.write(SerializationUtils.serialize(request));
        out.flush();
    }

    /**
     * Receives and deserializes a response from the server.
     * Prints the response message to the console.
     *
     * @throws IOException if the connection is closed or I/O fails.
     */
    protected void receiveResponse() throws IOException {
        byte[] buf = new byte[1500];
        if (in.read(buf) == -1) {
            throw new IOException("Connection closed by server");
        }
        Response response = SerializationUtils.deserialize(buf);
        console.writeln(response.getMessage());
    }

    /**
     * Re-establishes connection to the server.
     *
     * @throws IOException if reconnection fails.
     */
    protected void reconnect() throws IOException {
        init();
        if (isConnected) {
            console.writeln("Client was reconnected to the server");
        }
    }

    /**
     * Closes the socket and associated streams.
     *
     * @throws IOException if an error occurs during closure.
     */
    @Override
    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            isConnected = false;
            out.close();
            in.close();
            socket.close();
            console.writeln("Connection was closed");
        }
    }
}
