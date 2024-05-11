package it.polimi.ingsw.server.rmi;

import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class RMIClientTest {
    @Test
    void notBound() throws RemoteException {
        new RMIServer(4564);
        assertThrows(
                RemoteException.class,
                () -> new RMIClient(1234)
        );
    }
}