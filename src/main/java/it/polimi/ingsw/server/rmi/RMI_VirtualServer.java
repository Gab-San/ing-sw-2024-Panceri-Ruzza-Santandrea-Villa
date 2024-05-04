package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_VirtualServer extends Remote {
    void connect(String nickname, VirtualClient client) throws IllegalStateException, RemoteException;
    void setNumOfPlayers(String nickname, VirtualClient client, int num) throws IllegalStateException, RemoteException;
    void disconnect(String nickname, VirtualClient client) throws IllegalStateException, RemoteException;
    void placeStartCard(String nickname, VirtualClient client, boolean placeOnFront) throws IllegalStateException, RemoteException;
    void chooseColor(String nickname, VirtualClient client, PlayerColor color) throws IllegalStateException, RemoteException;
    void chooseObjective(String nickname, VirtualClient client, int choice) throws IllegalStateException, RemoteException;
    void placeCard(String nickname, VirtualClient client, String cardID, Point placePos, CornerDirection cornerDir) throws IllegalStateException, RemoteException;
    void draw(String nickname, VirtualClient client, char deck, int card) throws IllegalStateException, RemoteException;
    void startGame(String nickname, VirtualClient client) throws IllegalStateException, RemoteException;
    void sendMsg(String nickname, VirtualClient client, String message) throws RemoteException;
    void ping() throws RemoteException;
}
