package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.rmi.RemoteException;

public interface TCPClientMessage extends TCPMessage{
    void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException;
}
