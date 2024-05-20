package it.polimi.ingsw.network;

import it.polimi.ingsw.Point;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CommandPassthrough extends Remote{
    void sendMsg(String msg) throws RemoteException;
    void connect(String nickname) throws IllegalStateException, RemoteException;
    void setNumOfPlayers(int num) throws RemoteException;
    void disconnect() throws IllegalStateException, IllegalArgumentException, RemoteException;
    void placeStartCard(boolean placeOnFront) throws RemoteException;
    void chooseColor(char color) throws RemoteException;
    void chooseObjective(int choice) throws RemoteException;
    void placeCard(String cardID, Point placePos, String cornerDir, boolean placeOnFront) throws RemoteException;
    void draw(char deck, int card) throws RemoteException;
    void restartGame(int numOfPlayers) throws RemoteException;
    void ping() throws RemoteException;

}
