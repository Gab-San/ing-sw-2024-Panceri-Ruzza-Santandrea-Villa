package it.polimi.ingsw.server;

import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.listener.GameListener;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualClient extends Remote, GameListener {
    void update(String msg) throws RemoteException;
    void ping() throws RemoteException;
    void updatePlayer(String nickname);
    void updatePlayer(String nickname, PlayerColor colour);
    void updatePlayer(String nickname, int playerTurn);
    void updatePlayer(String nickname, boolean isConnected);

}
