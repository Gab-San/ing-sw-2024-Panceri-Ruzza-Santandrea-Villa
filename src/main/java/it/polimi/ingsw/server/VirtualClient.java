package it.polimi.ingsw.server;

import it.polimi.ingsw.model.enums.GamePhase;
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

    void deckReveal(char deck, String revealedId, int cardPosition) throws RemoteException;

    void createDeck(char deck, String topId, String firstId, String secondId) throws RemoteException;

    void deckUpdate(char deck, String cardID) throws RemoteException;

    void emptyDeck(char deck) throws RemoteException;

    void updatePhase(GamePhase gamePhase) throws RemoteException;

    void updateScore(String nickname, int score) throws RemoteException;

    void updateTurn(int currentTurn) throws RemoteException;
}
