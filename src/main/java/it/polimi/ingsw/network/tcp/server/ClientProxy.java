package it.polimi.ingsw.network.tcp.server;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPServerCheckMessage;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;
import it.polimi.ingsw.network.tcp.message.commands.PingMessage;
import it.polimi.ingsw.network.tcp.message.commands.SendMessage;
import it.polimi.ingsw.network.tcp.message.error.DisconnectErrorMessage;
import it.polimi.ingsw.network.tcp.message.error.ErrorMessage;
import it.polimi.ingsw.network.tcp.message.notifications.board.*;
import it.polimi.ingsw.network.tcp.message.notifications.deck.*;
import it.polimi.ingsw.network.tcp.message.notifications.playarea.PlayAreaFreeCornUpdateMessage;
import it.polimi.ingsw.network.tcp.message.notifications.playarea.PlayAreaPlaceCardMessage;
import it.polimi.ingsw.network.tcp.message.notifications.playarea.PlayAreaStateMessage;
import it.polimi.ingsw.network.tcp.message.notifications.playarea.PlayAreaVResUpdateMessage;
import it.polimi.ingsw.network.tcp.message.notifications.player.*;
import it.polimi.ingsw.network.tcp.message.notifications.playerhand.*;

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

    void sendNotification(TCPServerMessage notification) throws RemoteException {
        try{
            outputStream.writeObject(notification);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            clientHandler.closeSocket();
            throw new RemoteException("Connection Lost " + e.getMessage());
        }
    }

//region VIRTUAL CLIENT IMPLEMENTATION
    @Override
    public synchronized void displayMessage(String messenger, String msg) throws RemoteException {
        ping();
        sendNotification(new SendMessage(messenger, msg));
    }

    @Override
    public synchronized void ping() throws RemoteException {
        try{
            outputStream.writeObject(new PingMessage());
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            clientHandler.closeSocket();
        }
    }

    @Override
    public synchronized void setBoardState(int currentTurn, Map<String, Integer> scoreboard, GamePhase gamePhase, Map<String, Boolean> playerDeadLock) throws RemoteException {
        sendNotification( new BoardStateMessage(currentTurn, scoreboard, gamePhase, playerDeadLock));
    }

    @Override
    public synchronized void updatePhase(GamePhase gamePhase) throws RemoteException {
        sendNotification( new UpdatePhaseMessage(gamePhase));
    }

    @Override
    public synchronized void updateScore(String nickname, int score) throws RemoteException {
        sendNotification( new UpdateScoreMessage(nickname, score));
    }

    @Override
    public synchronized void updateTurn(int currentTurn) throws RemoteException {
        sendNotification(new UpdateTurnMessage(currentTurn));
    }

    @Override
    public synchronized void playerDeadLockUpdate(String nickname, boolean isDeadLocked) throws RemoteException {
        sendNotification(new PlayerDeadLockMessage(nickname, isDeadLocked));
    }

    @Override
    public synchronized void notifyEndgame() throws RemoteException {
        sendNotification(new EndgameMessage());
    }

    @Override
    public synchronized void notifyEndgame(String nickname, int score) throws RemoteException {
        sendNotification(new EndgameMessage(nickname, score));
    }

    @Override
    public synchronized void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {
        sendNotification(new PlayerStateMessage(nickname, isConnected, turn, colour));
    }

    @Override
    public synchronized void updatePlayer(String nickname, PlayerColor colour) throws RemoteException {
        sendNotification(new SetColorMessage(nickname, colour));
    }

    @Override
    public synchronized void updatePlayer(String nickname, int playerTurn) throws RemoteException {
        sendNotification(new SetTurnMessage(nickname, playerTurn));
    }

    @Override
    public synchronized void updatePlayer(String nickname, boolean isConnected) throws RemoteException {
        sendNotification(new SetConnectionMessage(nickname, isConnected));
    }

    @Override
    public synchronized void removePlayer(String nickname) throws RemoteException {
        sendNotification(new PlayerRemovalMessage(nickname));
    }

    @Override
    public synchronized void setDeckState(char deck, String topId, String firstId, String secondId) throws RemoteException {
        sendNotification(new DeckStateFullDrawableMessage(deck, topId, firstId, secondId));
    }

    @Override
    public synchronized void setDeckState(char deck, String revealedId, int cardPosition) throws RemoteException {
        sendNotification(new DeckStateMessage(deck, revealedId, cardPosition));
    }

    @Override
    public synchronized void setDeckState(char deck, String firstCardId, String secondCardId) throws RemoteException {
        sendNotification(new DeckStateOnlyRevealedMessage(deck, firstCardId, secondCardId));
    }

    @Override
    public synchronized void setEmptyDeckState(char deck) throws RemoteException {
        sendNotification(new DeckStateMessage(deck));
    }

    @Override
    public synchronized void deckUpdate(char deck, String revealedId, int cardPosition) throws RemoteException {
        sendNotification(new DeckCardUpdateMessage(deck, revealedId, cardPosition));
    }

    @Override
    public void emptyReveal(char deck, int cardPosition) throws RemoteException {
        sendNotification(new DeckEmptyMessage(deck, cardPosition));
    }

    @Override
    public synchronized void emptyFaceDownPile(char deck) throws RemoteException {
        sendNotification(new DeckEmptyMessage(deck));
    }

    @Override
    public synchronized void setPlayerHandState(String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) throws RemoteException {
        sendNotification(new PlayerHandStateMessage(nickname, playCards, objectiveCards, startingCard));
    }

    @Override
    public synchronized void playerHandAddedCardUpdate(String nickname, String drawnCardId) throws RemoteException {
        sendNotification(new PlayerHandAddCardMessage(nickname, drawnCardId));
    }

    @Override
    public synchronized void playerHandRemoveCard(String nickname, String playCardId) throws RemoteException {
        sendNotification(new PlayerHandRemoveCardMessage(nickname, playCardId));
    }

    @Override
    public synchronized void playerHandAddObjective(String nickname, String objectiveCard) throws RemoteException {
        sendNotification(new PlayerHandAddObjectiveMessage(nickname, objectiveCard));
    }

    @Override
    public synchronized void playerHandChooseObject(String nickname, String chosenObjectiveId) throws RemoteException {
        sendNotification(new PlayerHandChooseObjectiveMessage(nickname, chosenObjectiveId));
    }

    @Override
    public synchronized void playerHandSetStartingCard(String nickname, String startingCardId) throws RemoteException {
        sendNotification(new PlayerHandSetStartingCardMessage(nickname, startingCardId));
    }

    @Override
    public synchronized void setPlayAreaState(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) throws RemoteException {
        sendNotification(new PlayAreaStateMessage(nickname, cardPositions, visibleResources, freeSerializableCorners));
    }

    @Override
    public synchronized void updatePlaceCard(String nickname, String placedCardId, int row, int col) throws RemoteException {
        sendNotification(new PlayAreaPlaceCardMessage(nickname, placedCardId, row, col));
    }

    @Override
    public synchronized void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) throws RemoteException {
        sendNotification(new PlayAreaVResUpdateMessage(nickname, visibleResources));
    }

    @Override
    public synchronized void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerializableCorners) throws RemoteException {
        sendNotification(new PlayAreaFreeCornUpdateMessage(nickname, freeSerializableCorners));
    }

    @Override
    public synchronized void reportError(String errorMessage) throws RemoteException {
        sendNotification(new ErrorMessage(errorMessage));
    }

    @Override
    public synchronized void notifyTimeoutDisconnect() throws RemoteException {
        sendNotification(new DisconnectErrorMessage());
    }


//endregion

    void setUsername(String nickname){
        this.nickname = nickname;
    }

    public synchronized void sendCheck(TCPServerCheckMessage message){
        try{
            ping();
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            clientHandler.closeSocket();
        }
    }
}
