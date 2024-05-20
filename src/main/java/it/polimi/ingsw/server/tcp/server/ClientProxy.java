package it.polimi.ingsw.server.tcp.server;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.message.commands.SendMessage;
import it.polimi.ingsw.server.tcp.message.TCPServerCheckMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ClientProxy implements VirtualClient {

    private final ObjectOutputStream outputStream;
    private final ClientHandler clientHandler;
    private String nickname;
    public ClientProxy(ClientHandler clientHandler, ObjectOutputStream outputStream) {
        this.clientHandler = clientHandler;
        this.outputStream = outputStream;
    }


    @Override
    public void update(String msg) throws RemoteException {
        ping();
        try{
            outputStream.writeObject(new SendMessage(nickname, msg));
            outputStream.flush();
        } catch (IOException e) {
            clientHandler.closeSocket();
            throw new RemoteException("Connection Lost " + e.getMessage());
        }
    }

    @Override
    public void ping() throws RemoteException {
        clientHandler.ping();
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
    public void setDeckState(char deck, String firstCardId, String secondCardId) throws RemoteException {

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
    public void playerHandAddedCardUpdate(String nickname, String drawnCardId) throws RemoteException {

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

    void setUsername(String nickname){
        this.nickname = nickname;
    }

    public void sendCheck(TCPServerCheckMessage message){
        try{
            ping();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            clientHandler.closeSocket();
        }
    }
}
