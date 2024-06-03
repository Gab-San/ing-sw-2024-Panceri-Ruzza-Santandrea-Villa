package it.polimi.ingsw.network.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * This class acts as a gateway accepting connections requests and then
 * handling different sockets with client handlers.
 */
public class TCPServerSocket {
    private final ServerSocket serverSocket;
    private final ExecutorService handlerPool;

    /**
     * Constructs the server.
     * @param connectionPort port on which tcp connections are processed
     * @throws IOException if an I/O error occurs while opening the socket
     * @throws IllegalArgumentException if the port parameter is out of bounds
     */
    public TCPServerSocket(int connectionPort) throws IOException, IllegalArgumentException {
        serverSocket = new ServerSocket(connectionPort);
        handlerPool = Executors.newCachedThreadPool();
        System.out.println("TCP server waiting for client on port "+ connectionPort +"...");
        startServer();
    }


    /**
     * Starts the tcp server thread that waits for connections
     * and passes control over to client handlers.
     */
    private void startServer() {
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

    /**
     * Closes the tcp server socket and the client handlers.
     */
    public void closeServer(){
        handlerPool.shutdown();
        try {
            if(!serverSocket.isClosed()) serverSocket.close();
        } catch (IOException ignored) {
        }
    }

}
