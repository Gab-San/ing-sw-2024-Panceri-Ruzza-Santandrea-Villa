package it.polimi.ingsw.server.tcp;

import java.io.IOException;

public class ClientRun {
    public static void main(String[] args) {
        try {
            new TCPClient("127.0.0.1",8888).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
