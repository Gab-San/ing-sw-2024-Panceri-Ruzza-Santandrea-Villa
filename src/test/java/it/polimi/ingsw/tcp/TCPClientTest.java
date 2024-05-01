package it.polimi.ingsw.tcp;

import it.polimi.ingsw.server.tcp.TCPClient;

import java.io.IOException;

class TCPClientTest {
    public static void main(String[] args){
        try {
            new TCPClient(args[0],Integer.parseInt(args[1])).start();
        } catch (IOException | NumberFormatException ioException){
            ioException.printStackTrace(System.err);
        }
    }
}