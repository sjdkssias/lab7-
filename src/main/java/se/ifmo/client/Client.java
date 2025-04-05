package se.ifmo.client;

import se.ifmo.client.console.Console;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class Client implements AutoCloseable{
    private SocketChannel socketChannel;
    private Console console;

    public void connect(String host, int port){
        SocketAddress serverAdr = new InetSocketAddress(host, port);
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(serverAdr);
            console.writeln("client was connected");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void close() throws IOException {
        try {
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
                console.writeln("connection was closed");
            }
        } catch (IOException e){
            throw e;
        }
    }

}
