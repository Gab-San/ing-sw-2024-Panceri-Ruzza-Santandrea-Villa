package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.PlayerColor;

import java.rmi.RemoteException;

public interface CommandPassthrough {
    void sendMsg(String cmd) throws RemoteException, ConnectionLostException;
    void connect(String nickname) throws IllegalStateException, RemoteException;
    void setNumOfPlayers(int num) throws IllegalStateException, RemoteException;
    void disconnect() throws IllegalStateException, RemoteException;
    void placeStartCard(boolean placeOnFront) throws IllegalStateException, RemoteException;
    void chooseColor(PlayerColor color) throws IllegalStateException, RemoteException;
    void chooseObjective(int choice) throws IllegalStateException, RemoteException;
    void placeCard(String cardID, Point placePos, CornerDirection cornerDir) throws IllegalStateException, RemoteException;
    void draw(char deck, int card) throws IllegalStateException, RemoteException;
    void startGame() throws IllegalStateException, RemoteException;
}
