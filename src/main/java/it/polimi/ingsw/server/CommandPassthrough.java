package it.polimi.ingsw.server;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;

import java.rmi.RemoteException;

public interface CommandPassthrough {
    void sendMsg(String msg) throws RemoteException;
    void connect(String nickname) throws IllegalStateException, RemoteException;
    void setNumOfPlayers(int num) throws IllegalStateException, RemoteException;
    void disconnect() throws IllegalStateException, RemoteException;
    void placeStartCard(boolean placeOnFront) throws IllegalStateException, RemoteException;
    void chooseColor(char color) throws IllegalStateException, RemoteException;
    void chooseObjective(int choice) throws IllegalStateException, RemoteException;
    void placeCard(String cardID, Point placePos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, RemoteException;
    void draw(char deck, int card) throws IllegalStateException, RemoteException;
    void startGame(int numPlayers) throws IllegalStateException, RemoteException;
}
