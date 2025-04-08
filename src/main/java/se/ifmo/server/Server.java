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
import org.apache.logging.log4j.LogManager;
public class Server implements AutoCloseable {
    private static final int PORT = 8080;
    private static final int BUFFER_SIZE = 1500;
    private static Logger logger = LogManager.getLogger(Server.class);
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private Console console;
    private ByteBuffer buf;

    public Server(Console console) {
        this.console = console;
        start();
    }

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

    private void acceptConnection() throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        Socket socket = client.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        logger.info("Connected to: " + remoteAddr);
    }


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
                buf.flip();
                byte[] data = new byte[buf.remaining()];
                buf.get(data).clear();
                Response response = Router.route(SerializationUtils.deserialize(data));
                key.attach(ByteBuffer.wrap(SerializationUtils.serialize(response)));
                key.interestOps(SelectionKey.OP_WRITE);
                logger.debug("Read {} from client ", data);
            }
        } catch (IOException e) {
            logger.error("Error reading from client");
            socketChannel.close();
        }
    }

    private void writeKey(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        socketChannel.write(buf);

        if (!buf.hasRemaining()) {
            buf.clear();
            key.attach(ByteBuffer.allocate(BUFFER_SIZE));
            key.interestOps(SelectionKey.OP_READ);
        }
    }

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

    private void save(){
        CollectionManager.getInstance().save();
        logger.info("Collection was saved to the file");
    }

    @Override
    public void close() throws IOException {
        save();
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
