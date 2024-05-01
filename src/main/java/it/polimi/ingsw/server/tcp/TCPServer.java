package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.server.CentralServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {
    private final ServerSocket serverSocket;

    //TODO decide if to throw an exception or handle it in the constructor
    public TCPServer(int serverPort) throws IOException {
        // Default number of connections is 50
        this.serverSocket = new ServerSocket(serverPort);
    }

    /**
     * Starts the TCP Server that will wait for connections
     */
    @Override
    public void run(){
        System.out.println("Server up and running...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client request by" + clientSocket.getInetAddress());
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                new ClientHandler(reader, writer, CentralServer.getSingleton()).start();
            } catch (IOException srvExc) {
                srvExc.printStackTrace(System.err);
            }
        }
    }
}
