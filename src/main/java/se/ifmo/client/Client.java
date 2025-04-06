package se.ifmo.client;

import se.ifmo.client.chat.Response;
import se.ifmo.client.console.Console;

import java.io.*;
import java.net.*;
import java.nio.*;


public class Client implements AutoCloseable{
    private static final String HOST = "localhost";
    private static final int PORT = 8080;


    private Socket socket;
    private Console console;
    private ByteBuffer buf;
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean isConnected;
    private ClientProcess clientProcess;

    public Client(Console console){
        this.console = console;
        init();
    }


    public void init(){
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(HOST, PORT));
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            isConnected = true;
        } catch (UnknownHostException e) {
            console.writeln("Unknown host to connect: " + e.getMessage());
            isConnected = false;
        } catch (IOException e) {
            console.writeln("Error initializing connection: " + e.getMessage());
            isConnected = false;
        }
    }

    public void start(){
        if (!isConnected){
            console.writeln("The client isn't connected to the server");
            return;
        }
        clientProcess = new ClientProcess(console);
        clientProcess.startProcess();
    }

    private void sendMessage(Response response){

    }
    

    private void reconnect() throws IOException {
        close();
        init();
        if (isConnected){
            console.writeln("Client was reconnected to the server");
        } else {
            console.writeln("Connection failed");
        }
    }
    @Override
    public void close() throws IOException {
        try {
            if (socket!= null && !socket.isClosed()) {
                isConnected = false;
                writer.close();
                reader.close();
                socket.close();
                console.writeln("Connection was closed");
            }
        } catch (IOException e){
            throw e;
        }
    }

}
