package se.ifmo.server;

import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.client.chat.Request;
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

public class Server implements AutoCloseable {
    private static final int PORT = 8080;
    private static final int BUFFER_SIZE = 1500;
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
        } catch (IOException e) {
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
                continue;
            }
            if (key.isAcceptable()) {
                acceptConnection();
            } else if (key.isReadable()) {
                readKey(key);
            } else if (key.isWritable()) {
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
        console.writeln("Connected to: " + remoteAddr);
    }


    private void readKey(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);

        try {
            int bytesRead = socketChannel.read(buf);

            if (bytesRead == -1) {
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
            }
        } catch (IOException e) {
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
        try {
            key.channel().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        key.cancel();
    }

    @Override
    public void close() throws IOException {
        if (selector != null) {
            selector.close();
        }
        if (serverSocketChannel != null) {
            serverSocketChannel.close();
        }
        CollectionManager.getInstance().save();
    }
}
