package it.polimi.ingsw.server;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VirtualClient extends Remote{
    void update(String msg) throws RemoteException;
    void ping() throws RemoteException;
    void updatePlayer(String nickname, PlayerColor colour) throws RemoteException;
    void updatePlayer(String nickname, int playerTurn) throws RemoteException;
    void updatePlayer(String nickname, boolean isConnected) throws RemoteException;

    void setDeckState(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException;

    void deckUpdate(char deck, String revealedId, int cardPosition) throws RemoteException;

    void setDeckState(char deck, String topId, String firstId, String secondId) throws RemoteException;
    void setDeckState(char deck, String revealedId, int cardPosition) throws RemoteException;
    void setDeckState(char deck, String firstId, String secondId) throws RemoteException;

    void emptyDeck(char deck) throws RemoteException;

    void updatePhase(GamePhase gamePhase) throws RemoteException;

    void updateScore(String nickname, int score) throws RemoteException;

    void updateTurn(int currentTurn) throws RemoteException;

    void emptyReveal(char deck, int cardPosition) throws RemoteException;

    void createEmptyDeck(char deck) throws RemoteException;

    void setPlayerHandState(String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) throws RemoteException;

    void playerHandDrawUpdate(String nickname, String drawnCardId) throws RemoteException;

    void playerHandRemoveCard(String nickname, String playCardId) throws RemoteException;

    void playerHandAddObjective(String nickname, String objectiveCard) throws RemoteException;

    void playerHandChooseObject(String nickname, String chosenObjectiveId) throws RemoteException;

    void playerHandSetStartingCard(String nickname, String startingCardId) throws RemoteException;
}
