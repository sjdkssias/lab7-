package se.ifmo.client;

import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.console.Console;

import java.io.*;
import java.net.*;


public class Client implements AutoCloseable {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    private Socket socket;
    private Console console;
    private InputStream in;
    private boolean isConnected;
    private OutputStream out;

    public Client(Console console) {
        this.console = console;
        init();
    }

    public void init() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(HOST, PORT));
            out = socket.getOutputStream();
            in = socket.getInputStream();
            isConnected = true;
        } catch (UnknownHostException e) {
            console.writeln("Unknown host to connect: " + e.getMessage());
            isConnected = false;
        } catch (IOException e) {
            console.writeln("Error initializing connection: " + e.getMessage());
            isConnected = false;
        }
    }

    public void start() {
        if (!isConnected) {
            console.writeln("The client isn't connected to the server");
            return;
        }
        (new ClientProcess(console, this)).startProcess();
    }

    protected void sendRequest(Request request) throws IOException {
        out.write(SerializationUtils.serialize(request));
        out.flush();
    }


    protected void receiveResponse() throws IOException{
        byte[] buf = new byte[1500];
        if (in.read(buf) == -1) {
            throw new IOException("Connection closed by server");
        }
        Response response = SerializationUtils.deserialize(buf);
        console.writeln(response.getMessage());
    }

    protected void reconnect() throws IOException {
        close();
        init();
        if (isConnected) {
            console.writeln("Client was reconnected to the server");
        } else {
            console.writeln("Connection failed");
        }
    }

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
