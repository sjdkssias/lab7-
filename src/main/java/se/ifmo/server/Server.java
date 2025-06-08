package se.ifmo.server;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.chat.Router;
import se.ifmo.client.console.Console;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.*;

import org.apache.logging.log4j.LogManager;

/**
 * A NIO-based server implementation that handles client connections and requests.
 * Implements {@link AutoCloseable} for proper resource management.
 * Uses a selector pattern to handle multiple connections efficiently.
 */
public class Server implements AutoCloseable, Runnable {

    /** Default server port number. */
    private static final int PORT = Integer.parseInt(System.getenv("SERVER_PORT"));
    /** Buffer size for network operations. */
    private static final int BUFFER_SIZE = 10000;
    /** Logger instance for server operations. */
    public static Logger logger = LogManager.getLogger(Server.class);
    /** Selector for managing multiple channels. */
    private Selector selector;
    /** Server socket channel for accepting connections. */
    private ServerSocketChannel serverSocketChannel;
    /** Console interface for server output. */
    private Console console;
    /** Buffer for temporary data storage. */
    private ByteBuffer buf;

    private final ForkJoinPool readerPool = new ForkJoinPool();
    private final ExecutorService processorPool = Executors.newFixedThreadPool(5);
    private final ForkJoinPool writePool = new ForkJoinPool();


    /**
     * Constructs a new server instance with the specified console.
     * Automatically starts the server upon construction.
     *
     * @param console the console interface for server output
     */
    public Server(Console console) {
        this.console = console;
        start();
    }

    /**
     * Initializes and starts the server.
     * Sets up the server socket channel and selector.
     * @throws RuntimeException if server fails to start
     */
    private void start() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("Server is starting successfully");
            console.writeln("Server is listening in port " + PORT);
        } catch (IOException e) {
            logger.log(Level.FATAL, "Failed to launch server", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                processKeys();
            } catch (IOException e){
                logger.info("Error of proseccing selection keys");
            }
        }
    }
    /**
     * Processes all ready selection keys.
     * Handles accept, read, and write operations for connected clients.
     *
     * @throws IOException if an I/O error occurs during key processing
     */
    protected void processKeys() throws IOException {
        selector.selectNow();

        Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
        while (selectedKeys.hasNext()) {
            SelectionKey key = selectedKeys.next();
            selectedKeys.remove();
            if (!key.isValid()) {
                logger.warn("Invalid key encountered: {}", key);
                continue;
            }
            if (key.isAcceptable()) {

                logger.info("Acceptable key detected. Accepting connection...");
                acceptConnection();
            } else if (key.isReadable()) {
                readerPool.submit(() -> {
                    synchronized (key) {
                        try {
                            readKey(key);
                        } catch (Exception e) {
                            logger.error("Read error", e);
                            closeConnection(key);
                        }
                    }
                });

            } else if (key.isWritable()) {
                writePool.submit(() -> {
                    synchronized (key) {
                        try {
                            writeKey(key);
                        } catch (Exception e) {
                            logger.error("Write error", e);
                            closeConnection(key);
                        }
                    }
                });

            }
        }
    }

    /**
     * Accepts a new client connection.
     * Configures the channel as non-blocking and registers it with the selector.
     *
     * @throws IOException if an I/O error occurs during connection acceptance
     */
    private void acceptConnection() throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        try {
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            Socket socket = client.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            logger.log(Level.INFO, "Connected to: " + remoteAddr);
            selector.wakeup();
        } catch (IOException e){
            logger.log(Level.ERROR, "IOException with connection");
        }


    }

    /**
     * Handles read operations for a selection key.
     * Deserializes incoming data and routes it for processing.
     *
     * @param key the selection key representing the client connection
     * @throws IOException if an I/O error occurs during reading
     */
    private void readKey(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            if (!socketChannel.isOpen()) {
                logger.warn("Tried to read from closed channel");
                return;
            }

            int bytesRead = socketChannel.read(buf);

            if (bytesRead == -1) {
                logger.info("Client disconnected");
                closeConnection(key);
                return;
            }

            if (bytesRead > 0) {
                buf.flip();
                byte[] data = new byte[buf.remaining()];
                buf.get(data);

                readerPool.submit(() -> {
                    try {
                        Request request = SerializationUtils.deserialize(data);

                        processorPool.submit(() -> {
                            try {
                                Response response = Router.route(request);
                                ByteBuffer respBuffer = ByteBuffer.wrap(SerializationUtils.serialize(response));
                                synchronized (key) {
                                    if (key.isValid()) {
                                        key.attach(respBuffer);
                                        key.interestOps(SelectionKey.OP_WRITE);
                                        selector.wakeup();
                                    }
                                }
                            } catch (Exception e) {
                                logger.error("Error during request processing", e);
                                closeConnection(key);
                            }
                        });
                    } catch (Exception e) {
                        logger.error("Error during request deserialization", e);
                        closeConnection(key);
                    }
                });
            }
        } catch (IOException e) {
            logger.error("Error reading from client", e);
            closeConnection(key);
        }
    }

    private void writeKey(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        if (buf == null) {
            key.interestOps(SelectionKey.OP_READ);
            return;
        }
        try {
            socketChannel.write(buf);

            if (!buf.hasRemaining()) {
                key.attach(null);
                key.interestOps(SelectionKey.OP_READ);
                selector.wakeup();
            }
        } catch (IOException e) {
            logger.error("Write error", e);
            closeConnection(key);
        }
    }


    private void closeConnection(SelectionKey key) {
        try {
            key.cancel();
            key.channel().close();
        } catch (IOException e) {
            logger.error("Error closing connection", e);
        }
    }

    @Override
    public void close() throws IOException {
        if (selector != null) {
            selector.close();
            logger.info("Selector was closed");
        }
        if (serverSocketChannel != null) {
            serverSocketChannel.close();
            logger.info("Channel was closed");
        }
        processorPool.shutdown();
        readerPool.shutdown();
        writePool.shutdown();
    }
}