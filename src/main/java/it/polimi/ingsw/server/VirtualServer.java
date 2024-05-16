package it.polimi.ingsw.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {
    void connect(String nickname, VirtualClient client) throws IllegalStateException, RemoteException;
    void setNumOfPlayers(String nickname, VirtualClient client, int num) throws RemoteException;
    void disconnect(String nickname, VirtualClient client)
            throws IllegalStateException, IllegalArgumentException, RemoteException;
    void placeStartCard(String nickname, VirtualClient client, boolean placeOnFront) throws RemoteException;
    void chooseColor(String nickname, VirtualClient client, char colour) throws RemoteException;
    void chooseObjective(String nickname, VirtualClient client, int choice) throws RemoteException;
    void placeCard(String nickname, VirtualClient client, String cardID,
                   int row, int col, String cornerDir, boolean placeOnFront) throws RemoteException;
    void draw(String nickname, VirtualClient client, char deck, int card) throws RemoteException;
    void restartGame(String nickname, VirtualClient client, int numOfPlayers) throws RemoteException;
    void sendMsg(String nickname, VirtualClient client, String message) throws RemoteException;
    void ping() throws RemoteException;
}
