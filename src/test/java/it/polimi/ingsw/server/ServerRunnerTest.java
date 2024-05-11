package it.polimi.ingsw.server;

import it.polimi.ingsw.server.rmi.RMIServer;
import it.polimi.ingsw.server.tcp.TCPServer;

import java.io.IOException;
import java.rmi.RemoteException;

public class ServerRunnerTest {
    public static void main(String[] args) {
        try {
            new RMIServer();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            new TCPServer(8888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
