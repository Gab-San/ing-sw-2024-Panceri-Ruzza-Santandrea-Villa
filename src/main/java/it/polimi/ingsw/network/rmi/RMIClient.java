package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.view.ModelUpdater;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class RMIClient extends UnicastRemoteObject implements VirtualClient {

    private final RMIServerProxy proxy;
    private ModelUpdater modelUpdater;

    public RMIClient(int registryPort) throws RemoteException, NotBoundException{
        this("localhost", registryPort);
    }
    public RMIClient(String registryIP, int registryPort) throws RemoteException, NotBoundException {
        super();
        Registry registry = LocateRegistry.getRegistry(registryIP, registryPort);
        VirtualServer server = (VirtualServer) registry.lookup(RMIServer.CANONICAL_NAME);
        this.proxy = new RMIServerProxy(this, server);
    }

    public synchronized void setModelUpdater(ModelUpdater modelUpdater){
        this.modelUpdater = modelUpdater;
    }

    @Override
    public synchronized void displayMessage(String messenger, String msg) throws RemoteException {
        if(modelUpdater != null)
            modelUpdater.displayMessage(messenger, msg);
        else
            System.out.println(messenger + ":  " + msg);
    }

    /**
     * If a call to this function successfully returns, then this client is still connected
     * @throws RemoteException on connection loss
     */
    @Override
    public synchronized void ping() throws RemoteException {
        return;
    }

    @Override
    public synchronized void setBoardState(int currentTurn, Map<String, Integer> scoreboard, GamePhase gamePhase, Map<String, Boolean> playerDeadLock) throws RemoteException {
        modelUpdater.setBoardState(currentTurn, scoreboard, gamePhase, playerDeadLock);
    }

    @Override
    public synchronized void updatePlayer(String nickname, PlayerColor colour) throws RemoteException {
        modelUpdater.updatePlayer(nickname,colour);
    }

    @Override
    public synchronized void updatePlayer(String nickname, int playerTurn) throws RemoteException {
        modelUpdater.updatePlayer(nickname,playerTurn);
    }

    @Override
    public synchronized void updatePlayer(String nickname, boolean isConnected) throws RemoteException {
        modelUpdater.updatePlayer(nickname,isConnected);
    }

    @Override
    public synchronized void removePlayer(String nickname) throws RemoteException {
        modelUpdater.removePlayer(nickname);
    }

    @Override
    public synchronized void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {
        modelUpdater.setPlayerState(nickname, isConnected, turn, colour);
    }

    @Override
    public synchronized void deckUpdate(char deck, String revealedId, int cardPosition) throws RemoteException {
        modelUpdater.deckUpdate(deck,revealedId,cardPosition);
    }

    @Override
    public synchronized void setDeckState(char deck, String topId, String firstId, String secondId) throws RemoteException {
        modelUpdater.setDeckState(deck,topId, firstId,secondId);
    }

    @Override
    public synchronized void setDeckState(char deck, String revealedId, int cardPosition) throws RemoteException {
        modelUpdater.setDeckState(deck,revealedId,cardPosition);
    }

    @Override
    public synchronized void setDeckState(char deck, String firstCardId, String secondCardId) throws RemoteException {
        modelUpdater.setDeckState(deck,firstCardId,secondCardId);
    }


    @Override
    public synchronized void updatePhase(GamePhase gamePhase) throws RemoteException {
        modelUpdater.updatePhase(gamePhase);
    }

    @Override
    public synchronized void updateScore(String nickname, int score) throws RemoteException {
        modelUpdater.updateScore(nickname,score);
    }

    @Override
    public synchronized void updateTurn(int currentTurn) throws RemoteException {
        modelUpdater.updateTurn(currentTurn);
    }

    @Override
    public synchronized void emptyReveal(char deck, int cardPosition) throws RemoteException {
        modelUpdater.emptyReveal(deck,cardPosition);
    }

    @Override
    public synchronized void emptyFaceDownPile(char deck) throws RemoteException {
        modelUpdater.emptyFaceDownPile(deck);
    }

    @Override
    public synchronized void setEmptyDeckState(char deck) throws RemoteException {
        modelUpdater.setEmptyDeckState(deck);
    }

    @Override
    public synchronized void setPlayerHandState(String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) throws RemoteException {
        modelUpdater.setPlayerHandState(nickname, playCards, objectiveCards, startingCard);
    }

    @Override
    public synchronized void playerHandAddedCardUpdate(String nickname, String drawnCardId) throws RemoteException {
        modelUpdater.playerHandAddedCardUpdate(nickname, drawnCardId);
    }

    @Override
    public synchronized void playerHandRemoveCard(String nickname, String playCardId) throws RemoteException {
        modelUpdater.playerHandRemoveCard(nickname, playCardId);
    }

    @Override
    public synchronized void playerHandAddObjective(String nickname, String objectiveCard) throws RemoteException {
        modelUpdater.playerHandAddObjective(nickname, objectiveCard);
    }

    @Override
    public synchronized void playerHandChooseObject(String nickname, String chosenObjectiveId) throws RemoteException {
        modelUpdater.playerHandChooseObject(nickname, chosenObjectiveId);
    }

    @Override
    public synchronized void playerHandSetStartingCard(String nickname, String startingCardId) throws RemoteException {
        modelUpdater.playerHandSetStartingCard(nickname, startingCardId);
    }

    @Override
    public synchronized void setPlayAreaState(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) throws RemoteException {
        modelUpdater.setPlayAreaState(nickname, cardPositions, visibleResources, freeSerializableCorners);
    }

    @Override
    public synchronized void updatePlaceCard(String nickname, String placedCardId, int row, int col) throws RemoteException {
        modelUpdater.updatePlaceCard(nickname, placedCardId, row, col);
    }

    @Override
    public synchronized void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) throws RemoteException {
        modelUpdater.visibleResourcesUpdate(nickname, visibleResources);
    }

    @Override
    public synchronized void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerializableCorners) throws RemoteException {
        modelUpdater.freeCornersUpdate(nickname, freeSerializableCorners);
    }


    @Override
    public synchronized void playerDeadLockUpdate(String nickname, boolean isDeadLocked) throws RemoteException {
        modelUpdater.playerDeadLockUpdate(nickname, isDeadLocked);
    }

    @Override
    public synchronized void notifyEndgame() throws RemoteException {
        modelUpdater.notifyEndgame();
    }

    @Override
    public synchronized void notifyEndgame(String nickname, int score) throws RemoteException {
        modelUpdater.notifyEndgame(nickname, score);
    }

    @Override
    public synchronized void reportError(String errorMessage) throws RemoteException {
        modelUpdater.reportError(errorMessage);
    }

    @Override
    public synchronized void notifyIndirectDisconnect() throws RemoteException {
        modelUpdater.notifyIndirectDisconnect();
    }

    public RMIServerProxy getProxy(){
        return proxy;
    }
}
