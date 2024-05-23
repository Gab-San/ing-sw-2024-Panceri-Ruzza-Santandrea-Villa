package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class RMIClient extends UnicastRemoteObject implements VirtualClient {

    private final RMIServerProxy proxy;

    public RMIClient(int registryPort) throws RemoteException, NotBoundException{
        this("localhost", registryPort);
    }
    public RMIClient(String registryIP, int registryPort) throws RemoteException, NotBoundException {
        super();
        Registry registry = LocateRegistry.getRegistry(registryIP, registryPort);
        VirtualServer server = (VirtualServer) registry.lookup(RMIServer.CANONICAL_NAME);
        this.proxy = new RMIServerProxy(this, server);
    }

    @Override
    public synchronized void update(String msg) throws RemoteException {
        System.out.println(msg); // temp function
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
    public synchronized void updatePlayer(String nickname, PlayerColor colour) throws RemoteException {

    }

    @Override
    public synchronized void updatePlayer(String nickname, int playerTurn) throws RemoteException {

    }

    @Override
    public synchronized void updatePlayer(String nickname, boolean isConnected) throws RemoteException {

    }

    @Override
    public synchronized void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {

    }

    @Override
    public synchronized void deckUpdate(char deck, String revealedId, int cardPosition) throws RemoteException {

    }

    @Override
    public synchronized void setDeckState(char deck, String topId, String firstId, String secondId) throws RemoteException {

    }

    @Override
    public synchronized void setDeckState(char deck, String revealedId, int cardPosition) throws RemoteException {

    }

    @Override
    public synchronized void setDeckState(char deck, String firstCardId, String secondCardId) throws RemoteException {

    }


    @Override
    public synchronized void emptyDeck(char deck) throws RemoteException {

    }

    @Override
    public synchronized void updatePhase(GamePhase gamePhase) throws RemoteException {

    }

    @Override
    public synchronized void updateScore(String nickname, int score) throws RemoteException {

    }

    @Override
    public synchronized void updateTurn(int currentTurn) throws RemoteException {

    }

    @Override
    public synchronized void emptyReveal(char deck, int cardPosition) throws RemoteException {

    }

    @Override
    public synchronized void setEmptyDeckState(char deck) throws RemoteException {

    }

    @Override
    public synchronized void setPlayerHandState(String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) throws RemoteException {

    }

    @Override
    public synchronized void playerHandAddedCardUpdate(String nickname, String drawnCardId) throws RemoteException {

    }

    @Override
    public synchronized void playerHandRemoveCard(String nickname, String playCardId) throws RemoteException {

    }

    @Override
    public synchronized void playerHandAddObjective(String nickname, String objectiveCard) throws RemoteException {

    }

    @Override
    public synchronized void playerHandChooseObject(String nickname, String chosenObjectiveId) throws RemoteException {

    }

    @Override
    public synchronized void playerHandSetStartingCard(String nickname, String startingCardId) throws RemoteException {

    }

    @Override
    public synchronized void createPlayArea(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) throws RemoteException {

    }

    @Override
    public synchronized void placeCard(String nickname, String placedCardId, int row, int col) throws RemoteException {

    }

    @Override
    public synchronized void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) throws RemoteException {

    }

    @Override
    public synchronized void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerializableCorners) throws RemoteException {

    }

    @Override
    public synchronized void setBoardState(int currentTurn, GamePhase gamePhase) throws RemoteException {

    }

    @Override
    public synchronized void playerDeadLockUpdate(String nickname, boolean isDeadLocked) throws RemoteException {

    }

    @Override
    public void reportError(String errorMessage) throws RemoteException {

    }

    public RMIServerProxy getProxy(){
        return proxy;
    }
}
