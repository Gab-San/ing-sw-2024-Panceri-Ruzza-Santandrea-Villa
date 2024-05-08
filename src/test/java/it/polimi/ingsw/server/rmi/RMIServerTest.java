package it.polimi.ingsw.server.rmi;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class RMIServerTest {
    public static void main(String[] args) {
        try {
            new RMIServer();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}