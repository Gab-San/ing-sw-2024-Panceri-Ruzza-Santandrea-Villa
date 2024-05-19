package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

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
    public void update(String msg) throws RemoteException {
        System.out.println(msg); // temp function
    }

    /**
     * If a call to this function successfully returns, then this client is still connected
     * @throws RemoteException on connection loss
     */
    @Override
    public void ping() throws RemoteException {
        return;
    }

    @Override
    public void updatePlayer(String nickname, PlayerColor colour) throws RemoteException {

    }

    @Override
    public void updatePlayer(String nickname, int playerTurn) throws RemoteException {

    }

    @Override
    public void updatePlayer(String nickname, boolean isConnected) throws RemoteException {

    }

    @Override
    public void setDeckState(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {

    }

    @Override
    public void deckUpdate(char deck, String revealedId, int cardPosition) throws RemoteException {

    }

    @Override
    public void setDeckState(char deck, String topId, String firstId, String secondId) throws RemoteException {

    }

    @Override
    public void setDeckState(char deck, String revealedId, int cardPosition) throws RemoteException {

    }

    @Override
    public void setDeckState(char deck, String firstId, String secondId) throws RemoteException {

    }

    @Override
    public void emptyDeck(char deck) throws RemoteException {

    }

    @Override
    public void updatePhase(GamePhase gamePhase) throws RemoteException {

    }

    @Override
    public void updateScore(String nickname, int score) throws RemoteException {

    }

    @Override
    public void updateTurn(int currentTurn) throws RemoteException {

    }

    @Override
    public void emptyReveal(char deck, int cardPosition) throws RemoteException {

    }

    @Override
    public void createEmptyDeck(char deck) throws RemoteException {

    }

    @Override
    public void setPlayerHandState(String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) throws RemoteException {

    }

    @Override
    public void playerHandDrawUpdate(String nickname, String drawnCardId) throws RemoteException {

    }

    @Override
    public void playerHandRemoveCard(String nickname, String playCardId) throws RemoteException {

    }

    @Override
    public void playerHandAddObjective(String nickname, String objectiveCard) throws RemoteException {

    }

    @Override
    public void playerHandChooseObject(String nickname, String chosenObjectiveId) throws RemoteException {

    }

    @Override
    public void playerHandSetStartingCard(String nickname, String startingCardId) throws RemoteException {

    }

    @Override
    public void createPlayArea(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) throws RemoteException {

    }

    @Override
    public void placeCard(String nickname, String placedCardId, int row, int col) throws RemoteException {

    }

    @Override
    public void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) throws RemoteException {

    }

    @Override
    public void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerialableCorners) throws RemoteException {

    }

    @Override
    public void setBoardState(int currentTurn, GamePhase gamePhase) throws RemoteException {

    }

    @Override
    public void playerDeadLockUpdate(String nickname, boolean isDeadLocked) throws RemoteException {

    }

    public RMIServerProxy getProxy(){
        return proxy;
    }
}
