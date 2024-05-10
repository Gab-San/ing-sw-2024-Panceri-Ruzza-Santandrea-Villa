package it.polimi.ingsw.server.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer extends Thread{
    private final ServerSocket serverSocket;
    private final ExecutorService handlerPool;

    public TCPServer(int objPort) throws IOException, IllegalArgumentException {
        serverSocket = new ServerSocket(objPort);
        handlerPool = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        System.out.println("Server is running...");
        try{
            while(!serverSocket.isClosed()){
                Socket connectionSocket = serverSocket.accept();
                System.out.println("A new client has connected!");
                ClientHandler handler = new ClientHandler(connectionSocket);
                handlerPool.execute(handler);
            }
        } catch (IOException exception){
            exception.printStackTrace(System.err);
            closeServer();
        }
    }

    public void closeServer(){
        handlerPool.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

}
