package it.polimi.ingsw.server;

import it.polimi.ingsw.model.enums.PlayerColor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualClient extends Remote{
    void update(String msg) throws RemoteException;
    void ping() throws RemoteException;
    void updatePlayer(String nickname, PlayerColor colour) throws RemoteException;
    void updatePlayer(String nickname, int playerTurn) throws RemoteException;
    void updatePlayer(String nickname, boolean isConnected) throws RemoteException;

    void createPlayer(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException;

    void deckReveal(char deck, String revealedId, int cardPosition);

    void createDeck(char deck, String topId, String firstId, String secondId);

    void deckUpdate(char deck, String cardID);

    void emptyDeck(char deck);
}
