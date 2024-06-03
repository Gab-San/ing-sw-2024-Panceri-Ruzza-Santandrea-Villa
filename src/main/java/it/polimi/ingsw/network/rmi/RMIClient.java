package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.rmi.update.*;
import it.polimi.ingsw.network.rmi.update.deck.*;
import it.polimi.ingsw.network.rmi.update.playarea.FreeCornersUpdate;
import it.polimi.ingsw.network.rmi.update.playarea.PlaceCardUpdate;
import it.polimi.ingsw.network.rmi.update.playarea.SetPlayAreaStateUpdate;
import it.polimi.ingsw.network.rmi.update.playarea.VisibleResourcesUpdate;
import it.polimi.ingsw.network.rmi.update.player.*;
import it.polimi.ingsw.view.ModelUpdater;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class implements VirtualClient interface using rmi connection protocol.
 */
public class RMIClient extends UnicastRemoteObject implements VirtualClient {

    private final RMIServerProxy proxy;
    private ModelUpdater modelUpdater;
    private final Queue<RMIUpdate> updateQueue;
    private boolean isOpen;

    /**
     * Constructs a rmi client establishing connection on localhost.
     * @param registryPort server port
     * @throws RemoteException if the object can't be exported or if the registry cannot be located
     * @throws NotBoundException if virtual server name is not bound
     */
    public RMIClient(int registryPort) throws RemoteException, NotBoundException{
        this("localhost", registryPort);
    }

    /**
     * Constructs a rmi client establishing connection on specified ip.
     * @param registryIP server ip
     * @param registryPort server port
     * @throws RemoteException if the object can't be exported or if the registry cannot be located
     * @throws NotBoundException if virtual server name is not bound
     */
    public RMIClient(String registryIP, int registryPort) throws RemoteException, NotBoundException {
        super();
        Registry registry = LocateRegistry.getRegistry(registryIP, registryPort);
        VirtualServer server = (VirtualServer) registry.lookup(RMIServer.CANONICAL_NAME);
        this.proxy = new RMIServerProxy(this, server);
        updateQueue = new LinkedBlockingQueue<>();
        isOpen = true;
    }

//region VIRTUAL CLIENT IMPLEMENTATION
    @Override
    public void displayMessage(String messenger, String msg) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.offer(new DisplayMessageUpdate(modelUpdater, messenger, msg));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void ping() throws RemoteException {
        return;
    }

    @Override
    public void setBoardState(int currentTurn, Map<String, Integer> scoreboard, GamePhase gamePhase, Map<String, Boolean> playerDeadLock) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new SetBoardStateUpdate(modelUpdater, currentTurn, scoreboard, gamePhase, playerDeadLock));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void updatePlayer(String nickname, PlayerColor colour) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new PlayerUpdate(modelUpdater, nickname, colour));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void updatePlayer(String nickname, int playerTurn) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new PlayerUpdate(modelUpdater, nickname, playerTurn));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void updatePlayer(String nickname, boolean isConnected) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new PlayerUpdate(modelUpdater, nickname, isConnected));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void removePlayer(String nickname) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new RemovePlayerUpdate(modelUpdater, nickname));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new SetPlayerStateUpdate(modelUpdater, nickname, isConnected, turn, colour));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void deckUpdate(char deck, String revealedId, int cardPosition) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new DeckUpdate(modelUpdater, deck, revealedId, cardPosition));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void setDeckState(char deck, String topId, String firstId, String secondId) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new SetDeckStateUpdate(modelUpdater, deck, topId, firstId, secondId));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void setDeckState(char deck, String revealedId, int cardPosition) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new OnlyRevealedSetDeckStateUpdate(modelUpdater, deck, revealedId, cardPosition));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void setDeckState(char deck, String firstCardId, String secondCardId) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new SetDeckStateUpdate(modelUpdater, deck, firstCardId, secondCardId));
            updateQueue.notifyAll();
        }
    }


    @Override
    public void updatePhase(GamePhase gamePhase) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new PhaseUpdate(modelUpdater, gamePhase));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void updateScore(String nickname, int score) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new ScoreUpdate(modelUpdater, nickname, score));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void updateTurn(int currentTurn) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new TurnUpdate(modelUpdater, currentTurn));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void emptyReveal(char deck, int cardPosition) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new EmptyRevealUpdate(modelUpdater, deck, cardPosition));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void emptyFaceDownPile(char deck) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new EmptyFaceDownPileUpdate(modelUpdater, deck));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void setEmptyDeckState(char deck) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new SetDeckStateUpdate(modelUpdater, deck));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void setPlayerHandState(String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new SetPlayerHandStateUpdate(modelUpdater, nickname, playCards, objectiveCards, startingCard));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void playerHandAddedCardUpdate(String nickname, String drawnCardId) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new HandAddedCardUpdate(modelUpdater, nickname, drawnCardId));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void playerHandRemoveCard(String nickname, String playCardId) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new HandRemoveCard(modelUpdater, nickname, playCardId));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void playerHandAddObjective(String nickname, String objectiveCard) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new HandAddObjectiveUpdate(modelUpdater, nickname, objectiveCard));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void playerHandChooseObject(String nickname, String chosenObjectiveId) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new HandChooseObjectiveUpdate(modelUpdater, nickname, chosenObjectiveId));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void playerHandSetStartingCard(String nickname, String startingCardId) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new HandSetStartingCard(modelUpdater, nickname, startingCardId));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void setPlayAreaState(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new SetPlayAreaStateUpdate(modelUpdater, nickname, cardPositions, visibleResources, freeSerializableCorners));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void updatePlaceCard(String nickname, String placedCardId, int row, int col, boolean placeOnFront) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new PlaceCardUpdate(modelUpdater, nickname, placedCardId, row, col, placeOnFront));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new VisibleResourcesUpdate(modelUpdater, nickname, visibleResources));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerializableCorners) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new FreeCornersUpdate(modelUpdater, nickname, freeSerializableCorners));
            updateQueue.notifyAll();
        }
    }


    @Override
    public void playerDeadLockUpdate(String nickname, boolean isDeadLocked) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new PlayerDeadLockUpdate(modelUpdater, nickname, isDeadLocked));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void notifyEndgame() throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new NotifyEndGameUpdate(modelUpdater));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void notifyEndgame(String nickname, int score) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new NotifyEndGameUpdate(modelUpdater, nickname, score));
            updateQueue.notifyAll();
        }
    }

    @Override
    public void reportError(String errorMessage) throws RemoteException {
        synchronized (updateQueue){
            updateQueue.add(new ReportErrorUpdate(modelUpdater, errorMessage));
            updateQueue.notifyAll();
        }  
    }

    @Override
    public void notifyIndirectDisconnect() throws RemoteException {
        close();
        synchronized (updateQueue){
            updateQueue.clear();
            updateQueue.add(new NotifyIndirectDisconnect(modelUpdater));
            updateQueue.notifyAll();
        }
    }
//endregion

//region INTERNAL FUNCTIONS

    /**
     * Returns client-side proxy.
     * @return rmi proxy
     */
    public RMIServerProxy getProxy(){
        return proxy;
    }

    /**
     * Since rmi connection cannot be effectively closed, this method
     * stops the update thread, so that calls to rmi client have no effect on the application.
     */
    public void close(){
        isOpen = false;
    }

    /**
     * Sets the instance of model updater
     * @param modelUpdater instance of model updater
     */
    public void setModelUpdater(ModelUpdater modelUpdater){
        this.modelUpdater = modelUpdater;
        startUpdateExecutor();
    }

    /**
     * Starts the thread responsible for applying the updates.
     */
    private void startUpdateExecutor() {
        new Thread(
                () -> {
                    while (isOpen){
                        RMIUpdate rmiUpdate;
                        synchronized (updateQueue) {
                            while (updateQueue.isEmpty()) {
                                try {
                                    updateQueue.wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            rmiUpdate = updateQueue.remove();
                        }
                        rmiUpdate.update();
                    }
                }
        ).start();
    }
//endregion
}
