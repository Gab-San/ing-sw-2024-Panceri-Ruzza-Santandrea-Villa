package it.polimi.ingsw.tcp;

import it.polimi.ingsw.server.tcp.TCPServer;

import java.io.IOException;

class TCPServerTest {
    public static void main(String[] args){
        try{
            new TCPServer(Integer.parseInt(args[0])).start();
        } catch (IOException ioException){
            ioException.printStackTrace(System.err);
        }
    }
}