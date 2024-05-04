package it.polimi.ingsw.server;

import java.rmi.RemoteException;

//TODO: create the actual VirtualClient
public interface VirtualClient {
    //FIXME: [Ale] review use of ConnectionLostException for TCP (RemoteException is needed for RMI)
    // [GAMBA] Adding a sendCmd() in order to have a silouette of the parser
    void update() throws RemoteException, ConnectionLostException;
    void ping() throws RemoteException, ConnectionLostException;
    void sendCmd(String cmd);
}
