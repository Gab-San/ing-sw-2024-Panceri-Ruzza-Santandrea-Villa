package it.polimi.ingsw.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualClient extends Remote {
    //FIXME: [Ale] review use of ConnectionLostException for TCP (RemoteException is needed for RMI)
    // [GAMBA] Adding a sendCmd() in order to have a silhouette of the parser
    void update(String msg) throws RemoteException, ConnectionLostException;
    void ping() throws RemoteException, ConnectionLostException;
}
