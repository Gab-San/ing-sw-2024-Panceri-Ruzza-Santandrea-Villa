package it.polimi.ingsw.stub;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class CountUpdateClient extends StubClient {
    int counter;

    public CountUpdateClient(String nickname, StubView view) {
        super(nickname, view);
    }

    void countFunc(){
        counter++;
        System.out.println(counter);
    }

    @Override
    public void displayMessage(String messenger, String msg) throws RemoteException {
        countFunc();
    }

    @Override
    public void ping() throws RemoteException {
         countFunc();
    }

    @Override
    public void setBoardState(int currentTurn, Map<String, Integer> scoreboard, GamePhase gamePhase, Map<String, Boolean> playerDeadLock) throws RemoteException {
        countFunc();
    }

    @Override
    public void updatePhase(GamePhase gamePhase) throws RemoteException {
        countFunc();
    }

    @Override
    public void updateScore(String nickname, int score) throws RemoteException {
        countFunc();
    }

    @Override
    public void updateTurn(int currentTurn) throws RemoteException {
        countFunc();
    }

    @Override
    public void playerDeadLockUpdate(String nickname, boolean isDeadLocked) throws RemoteException {
        countFunc();
    }

    @Override
    public void notifyEndgame() throws RemoteException {
        countFunc();
    }

    @Override
    public void notifyEndgame(String nickname, int score) throws RemoteException {
        countFunc();
    }

    @Override
    public void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {
        countFunc();
    }

    @Override
    public void updatePlayer(String nickname, PlayerColor colour) throws RemoteException {
        countFunc();
    }

    @Override
    public void updatePlayer(String nickname, int playerTurn) throws RemoteException {
        countFunc();
    }

    @Override
    public void updatePlayer(String nickname, boolean isConnected) throws RemoteException {
        countFunc();
    }

    @Override
    public void removePlayer(String nickname) throws RemoteException {
        countFunc();
    }

    @Override
    public void setDeckState(char deck, String topId, String firstId, String secondId) throws RemoteException {
        countFunc();
    }

    @Override
    public void setDeckState(char deck, String revealedId, int cardPosition) throws RemoteException {
        countFunc();
    }

    @Override
    public void setDeckState(char deck, String firstCardId, String secondCardId) throws RemoteException {
        countFunc();
    }

    @Override
    public void setEmptyDeckState(char deck) throws RemoteException {
        countFunc();
    }

    @Override
    public void deckUpdate(char deck, String revealedId, int cardPosition) throws RemoteException {
        countFunc();
    }

    @Override
    public void emptyReveal(char deck, int cardPosition) throws RemoteException {
        countFunc();
    }

    @Override
    public void emptyFaceDownPile(char deck) throws RemoteException {
        countFunc();
    }

    @Override
    public void setPlayerHandState(String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) throws RemoteException {
        countFunc();
    }

    @Override
    public void playerHandAddedCardUpdate(String nickname, String drawnCardId) throws RemoteException {
        countFunc();
    }

    @Override
    public void playerHandRemoveCard(String nickname, String playCardId) throws RemoteException {
        countFunc();
    }

    @Override
    public void playerHandAddObjective(String nickname, String objectiveCard) throws RemoteException {
        countFunc();
    }

    @Override
    public void playerHandChooseObject(String nickname, String chosenObjectiveId) throws RemoteException {
        countFunc();
    }

    @Override
    public void playerHandSetStartingCard(String nickname, String startingCardId) throws RemoteException {
        countFunc();
    }

    @Override
    public void setPlayAreaState(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) throws RemoteException {
        countFunc();
    }

    @Override
    public void updatePlaceCard(String nickname, String placedCardId, int row, int col, boolean placeOnFront) throws RemoteException {
        countFunc();
    }

    @Override
    public void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) throws RemoteException {
        countFunc();
    }

    @Override
    public void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerializableCorners) throws RemoteException {
        countFunc();
    }

    @Override
    public void reportError(String errorMessage) throws RemoteException {
        countFunc();
    }

    @Override
    public void notifyIndirectDisconnect() throws RemoteException {
        countFunc();
    }
}
