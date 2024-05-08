package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.server.CentralServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {
    private final ServerSocket objectSocket;
    private final ServerSocket messageSocket;

    public TCPServer(int objPort, int msgPort) throws IOException {
        this.objectSocket = new ServerSocket(objPort);
        this.messageSocket = new ServerSocket(msgPort);
    }


    //FIXME: The problem with this version is Exception Handling
    @Override
    public void run() {
        while (true){
            try{
                Socket objectConnectionSocket = objectSocket.accept();
                System.out.println("New connection on object socket");
                Socket messageConnectionSocket = messageSocket.accept();
                System.out.println("New client request by" + messageConnectionSocket.getInetAddress());
                // Object Output Stream must be created before inputStream
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(objectConnectionSocket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(objectConnectionSocket.getInputStream());
                PrintWriter writer = new PrintWriter(messageConnectionSocket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(messageConnectionSocket.getInputStream()));
                new ClientHandler(objectInputStream, objectOutputStream, writer, reader, CentralServer.getSingleton()).start();
            } catch (IOException srvExc) {
                //TODO Handle Exception
                srvExc.printStackTrace(System.err);
                break;
            }
        }
    }
}
