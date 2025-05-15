package se.ifmo.server;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.Logger;
import se.ifmo.client.chat.Response;
import se.ifmo.client.chat.Router;
import se.ifmo.client.console.Console;
import se.ifmo.server.collectionManagement.CollectionManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.ForkJoinPool;

import org.apache.logging.log4j.LogManager;

/**
 * A NIO-based server implementation that handles client connections and requests.
 * Implements {@link AutoCloseable} for proper resource management.
 * Uses a selector pattern to handle multiple connections efficiently.
 */
public class Server implements AutoCloseable, Runnable {
    /** Default server port number. */
    private static final int PORT = 8080;
    /** Buffer size for network operations. */
    private static final int BUFFER_SIZE = 1500;
    /** Logger instance for server operations. */
    private static Logger logger = LogManager.getLogger(Server.class);
    /** Selector for managing multiple channels. */
    private Selector selector;
    /** Server socket channel for accepting connections. */
    private ServerSocketChannel serverSocketChannel;
    /** Console interface for server output. */
    private Console console;
    /** Buffer for temporary data storage. */
    private ByteBuffer buf;

    private final ForkJoinPool readRequestPool = new ForkJoinPool();

    private final ForkJoinPool sendResponsePool = new ForkJoinPool();

    private final ForkJoinPool acceptConnectionPoll = new ForkJoinPool();
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
        } catch (IOException e) {
            logger.fatal("Error with starting server");
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
                logger.info("Readable key detected. Processing read...");
                readKey(key);
            } else if (key.isWritable()) {
                logger.info("Writable key detected. Processing write...");
                writeKey(key);
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
        acceptConnectionPoll.execute(() -> {
            try {
                client.configureBlocking(false);
                client.register(selector, SelectionKey.OP_READ);
                Socket socket = client.socket();
                SocketAddress remoteAddr = socket.getRemoteSocketAddress();
                logger.info("Connected to: " + remoteAddr);
                selector.wakeup();
            } catch (IOException e){
                logger.error("");
            }
        });

    }

    /**
     * Handles read operations for a selection key.
     * Deserializes incoming data and routes it for processing.
     *
     * @param key the selection key representing the client connection
     * @throws IOException if an I/O error occurs during reading
     */
    private void readKey(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);

        try {
            int bytesRead = socketChannel.read(buf);

            if (bytesRead == -1) {
                logger.info("Client was disconnected");
                closeConnection(key);
                return;
            }

            if (bytesRead > 0) {
                readRequestPool.execute(() -> {
                    buf.flip();
                    byte[] data = new byte[buf.remaining()];
                    buf.get(data).clear();
                    Response response = Router.route(SerializationUtils.deserialize(data));
                    key.attach(ByteBuffer.wrap(SerializationUtils.serialize(response)));
                    key.interestOps(SelectionKey.OP_WRITE);
                    logger.debug("Read {} from client ", data);
                    selector.wakeup();
                });
            }
        } catch (IOException e) {
            logger.error("Error reading from client");
            socketChannel.close();
        }
    }

    /**
     * Handles write operations for a selection key.
     * Sends response data back to the client.
     *
     * @param key the selection key representing the client connection
     * @throws IOException if an I/O error occurs during writing
     */
    private void writeKey(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();

        sendResponsePool.execute(() -> {
            try {
                socketChannel.write(buf);

                if (!buf.hasRemaining()) {
                    buf.clear();
                    key.attach(ByteBuffer.allocate(BUFFER_SIZE));
                    key.interestOps(SelectionKey.OP_READ);
                    selector.wakeup();
                }
            } catch (IOException e){
                logger.error("");
                closeConnection(key);
            }
        });

    }

    /**
     * Closes a client connection and cleans up resources.
     * Automatically saves the collection before closing.
     *
     * @param key the selection key representing the client connection
     */
    private void closeConnection(SelectionKey key) {
        save();
        try {
            logger.info("Client disconnected");
            key.channel().close();
        } catch (IOException e) {
            logger.error("Error with closing connection");
            throw new RuntimeException(e);
        }
        key.cancel();
    }

    /**
     * Saves the current collection state to persistent storage.
     * Delegates to the CollectionManager's save method.
     */
    private void save() {
        CollectionManager.getInstance().save();
        logger.info("Dragon was saved to the database");
    }

    /**
     * Closes all server resources including selector and server socket channel.
     * Automatically saves the collection before closing.
     *
     * @throws IOException if an I/O error occurs during closure
     */
    @Override
    public void close() throws IOException {
        save();
        acceptConnectionPoll.shutdown();
        readRequestPool.shutdown();
        sendResponsePool.shutdown();
        if (selector != null) {
            selector.close();
            logger.info("Selector was closed");
        }
        if (serverSocketChannel != null) {
            serverSocketChannel.close();
            logger.info("Channel was closed");
        }
    }
}