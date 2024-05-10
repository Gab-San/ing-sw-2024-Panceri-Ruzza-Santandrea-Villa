package it.polimi.ingsw.server.tcp;

import java.io.IOException;

public class ServerRun {
    public static void main(String[] args) {
        try {
            new TCPServer(8888).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
