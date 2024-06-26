package it.polimi.ingsw.stub;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class acts as a simpler server. Used to test out basic
 * client server interactions.
 */
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
                            handler = new ConnectionHandler(connectionSocket);
                            handler.run();
                        }
                    } catch (IOException exception) {
                        System.err.println(exception.getMessage());
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

/**
 * This class acts as a simpler client handler that only prints out
 * read objects.
 */
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
            System.out.println(obj);
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
