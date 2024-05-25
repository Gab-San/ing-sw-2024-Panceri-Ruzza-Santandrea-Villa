package it.polimi.ingsw.network.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class TCPServerSocket {
    private final ServerSocket serverSocket;
    private final ExecutorService handlerPool;

    public TCPServerSocket(int objPort) throws IOException, IllegalArgumentException {
        serverSocket = new ServerSocket(objPort);
        handlerPool = Executors.newCachedThreadPool();
        startServer();
    }


    private void startServer() {
        System.out.println("TCP server waiting for client...");
        new Thread(
                () -> {
                    try {
                        while (!serverSocket.isClosed()) {
                            Socket connectionSocket = serverSocket.accept();
                            System.out.println("A new client has connected!");
                            ClientHandler handler = new ClientHandler(connectionSocket);
                            handlerPool.execute(handler);
                        }
                    } catch (RejectedExecutionException ignore){} catch (IOException exception) {
                        System.err.println("Closing server: " + exception.getMessage());
                        closeServer();
                    }
                }
        ).start();
    }

    public void closeServer(){
        handlerPool.shutdown();
        try {
            if(!serverSocket.isClosed()) serverSocket.close();
        } catch (IOException ignored) {
        }
    }

}
