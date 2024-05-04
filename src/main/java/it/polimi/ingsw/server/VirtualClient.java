package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.PlayerColor;

import java.rmi.Remote;
import java.rmi.RemoteException;

//TODO: create the actual VirtualClient
public interface VirtualClient extends Remote {
    //FIXME: [Ale] review use of ConnectionLostException for TCP (RemoteException is needed for RMI)
    // [GAMBA] Adding a sendCmd() in order to have a silouette of the parser
    void update(String msg) throws RemoteException, ConnectionLostException;
    void ping() throws RemoteException, ConnectionLostException;
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
