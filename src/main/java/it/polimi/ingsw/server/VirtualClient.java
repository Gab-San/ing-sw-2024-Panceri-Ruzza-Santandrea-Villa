package it.polimi.ingsw.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualClient extends Remote {
    //FIXME: [GAMBA] Adding a sendCmd() in order to have a silhouette of the parser
    void update(String msg) throws RemoteException;
    void ping() throws RemoteException;
}
