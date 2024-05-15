package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.server.CommandPassthrough;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements VirtualClient, CommandPassthrough {
    private String nickname = null;
    private final VirtualServer server;

    public RMIClient(int registryPort) throws RemoteException, NotBoundException{
        this("localhost", registryPort);
    }
    public RMIClient(String registryIP, int registryPort) throws RemoteException, NotBoundException {
        super();
        Registry registry = LocateRegistry.getRegistry(registryIP, registryPort);
        server = (VirtualServer) registry.lookup(RMIServer.CANONICAL_NAME);
    }

    @Override
    public void update(String msg) throws RemoteException {
        System.out.println(msg); // temp function
    }

    /**
     * If a call to this function successfully returns, then this client is still connected
     * @throws RemoteException on connection loss
     */
    @Override
    public void ping() throws RemoteException {
        return;
    }

    //TODO In case remove this
    @Override
    public void sendMsg(String msg) throws RemoteException {
        //System.out.println("Sending Message: " + msg);
        validateConnection();
        server.sendMsg(nickname, this, msg);
    }

    private void validateConnection() throws IllegalStateException, RemoteException{
        if(nickname == null){
            throw new IllegalStateException("Please connect to server before sending commands other than 'connect'.");
        }
        else server.ping(); // throws RemoteException on connection lost.
    }

    @Override
    public void connect(String nickname) throws IllegalStateException, RemoteException {
        server.connect(nickname, this);
        this.nickname = nickname;
    }
    @Override
    public void disconnect() throws IllegalStateException, RemoteException {
        validateConnection();
        server.disconnect(nickname, this);
    }

    @Override
    public void setNumOfPlayers(int num) throws IllegalStateException, RemoteException {
        validateConnection();
        server.setNumOfPlayers(nickname, this, num);
    }
    @Override
    public void placeStartCard(boolean placeOnFront) throws IllegalStateException, RemoteException {
        validateConnection();
        server.placeStartCard(nickname, this, placeOnFront);
    }
    @Override
    public void chooseColor(char color) throws IllegalStateException, RemoteException{
        validateConnection();
        server.chooseColor(nickname, this, color);
    }
    @Override
    public void chooseObjective(int choice) throws IllegalStateException, RemoteException {
        validateConnection();
        server.chooseObjective(nickname, this, choice);
    }

    @Override
    public void placeCard(String cardID, Point placePos, String cornerDir, boolean placeOnFront) throws IllegalStateException, RemoteException {
        validateConnection();
        server.placeCard(nickname, this, cardID, placePos.row(),
                placePos.col(), cornerDir, placeOnFront);
    }
    @Override
    public void draw(char deck, int card) throws IllegalStateException, RemoteException {
        validateConnection();
        server.draw(nickname, this, deck, card);
    }

    @Override
    public void startGame(int numOfPlayers) throws IllegalStateException, RemoteException {
        validateConnection();
        server.startGame(nickname, this, numOfPlayers);
    }
}
