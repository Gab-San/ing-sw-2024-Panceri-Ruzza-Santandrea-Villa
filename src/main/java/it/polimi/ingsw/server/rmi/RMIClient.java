package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.server.ConnectionLostException;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements VirtualClient {
    private String nickname;
    private final RMIServer server;

    public RMIClient() throws RemoteException, NotBoundException{
        this("localhost");
    }
    public RMIClient(String registryIP) throws RemoteException, NotBoundException {
        super();
        Registry registry = LocateRegistry.getRegistry(registryIP, RMIServer.REGISTRY_PORT);
        server = (RMIServer) registry.lookup(RMIServer.CANONICAL_NAME);
    }

    @Override
    public void update() throws RemoteException {
        System.out.println("Update Received"); // temp function
    }

    /**
     * If a call to this function successfully returns, then this client is still connected
     * @throws RemoteException on connection loss
     */
    @Override
    public void ping() throws RemoteException {
        return;
    }

    public void connect(String nickname) throws IllegalStateException, RemoteException {
        server.connect(nickname, this);
        this.nickname = nickname;
    }

    void setNumOfPlayers(int num) throws IllegalStateException{
        server.setNumOfPlayers(nickname, this, num);
    }

    public void disconnect() throws IllegalStateException, RemoteException {
        server.disconnect(nickname, this);
    }
}
