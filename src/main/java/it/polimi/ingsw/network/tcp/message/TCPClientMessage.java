package it.polimi.ingsw.network.tcp.message;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;

import java.rmi.RemoteException;

public interface TCPClientMessage extends TCPMessage {
    void execute(VirtualServer virtualServer, VirtualClient virtualClient)
            throws IllegalStateException, IllegalArgumentException, RemoteException;
}
