package it.polimi.ingsw.server.tcp.testingStub;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PuppetServer {
    private final ServerSocket serverSocket;
    private ConnectionHandler handler;

    public PuppetServer(int objPort) throws IOException, IllegalArgumentException {
        serverSocket = new ServerSocket(objPort);
        startServer();
    }


    public void startServer() {
        System.out.println("Server is running...");
        new Thread(
                () -> {
                    try {
                        while (!serverSocket.isClosed()) {
                            Socket connectionSocket = serverSocket.accept();
                            System.out.println("A new client has connected!");
                            handler = new ConnectionHandler(connectionSocket);
                            handler.run();
                        }
                    } catch (IOException exception) {
//                        exception.printStackTrace(System.err);
                        closeServer();
                    }
                }
        ).start();
    }

    public void closeServer(){
        handler.close();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

}

class ConnectionHandler {
    Socket clientSocket;
    ConnectionHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    public void run(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            Object obj = inputStream.readObject();
        } catch (IOException e) {
            close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    void close(){
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
