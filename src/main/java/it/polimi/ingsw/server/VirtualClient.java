package it.polimi.ingsw.server;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualClient extends Remote {
    void update(String msg) throws RemoteException;
    void ping() throws RemoteException;
}
