package it.polimi.ingsw.network.tcp.client;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.*;
import it.polimi.ingsw.network.tcp.message.TCPServerCheckMessage;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;
import it.polimi.ingsw.view.ModelUpdater;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.*;

public class TCPClientSocket implements VirtualClient{

    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final Queue<TCPServerMessage> updateQueue;
    private final ServerProxy proxy;
    private ModelUpdater modelUpdater;

    public TCPClientSocket(int port) throws IOException {
        this("localhost", port);
    }

    public TCPClientSocket(String hostAddr, int connectionPort) throws IOException{
        this.clientSocket = new Socket(hostAddr, connectionPort);
        this.proxy = new ServerProxy(new ObjectOutputStream(clientSocket.getOutputStream()), this);
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
        updateQueue = new LinkedBlockingQueue<>();
        startReader();
        startCommandExecutor();
    }
//region SOCKET THREADS

    private void startCommandExecutor(){
        new Thread(
                () -> {
                    while (!clientSocket.isClosed()){
                        TCPServerMessage command;
                        synchronized (updateQueue) {
                            while (updateQueue.isEmpty()) {
                                try {
                                    updateQueue.wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            command = updateQueue.remove();
                        }
                        try {
                            command.execute(this);
                        } catch (RemoteException e) {
                            System.err.println(e.getMessage() + "\n" + e.getCause().getMessage());
                            closeSocket();
                        }
                    }
                }
        ).start();
    }


    public void startReader() {
        new Thread(
                () -> {
                    //System.out.println("Socket connection started!");
                    try {
                        while (!clientSocket.isClosed()) {
                            TCPMessage commandFromServer;
                            commandFromServer = (TCPMessage) inputStream.readObject();
                            if (!commandFromServer.isCheck()) {
                                synchronized (updateQueue) {
                                    updateQueue.offer((TCPServerMessage) commandFromServer);
                                    updateQueue.notifyAll();
                                }
                            } else {
                                proxy.addCheck((TCPServerCheckMessage) commandFromServer);
                            }
                        }
                    } catch (IOException e) {
                        closeSocket();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).start();
    }

//endregion

//region FOR TEST PURPOSES
    public boolean isClosed(){
        return clientSocket.isClosed();
    }

//    void sendObject(Object obj){
//        try{
//            outputStream.writeObject(obj);
//            outputStream.flush();
//        } catch (IOException exception){
//            closeSocket();
//        }
//    }

//endregion

//region SOCKET FUNCTIONS
    public void closeSocket(){
        try{
            if(!clientSocket.isClosed()) {
                if (inputStream != null) inputStream.close();

                clientSocket.close();

            }
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public ServerProxy getProxy(){
        return proxy;
    }
//endregion

    public void setModelUpdater(ModelUpdater modelUpdater) {
        this.modelUpdater = modelUpdater;
    }

//region VIRTUAL CLIENT INTERFACE
    @Override
    public void update(String msg){
        System.out.println(msg);
        System.out.flush();
    }

    @Override
    public void ping() throws RemoteException {
        return;
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
    public synchronized void emptyDeck(char deck) throws RemoteException {
        modelUpdater.emptyDeck(deck);
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
    public synchronized void createPlayArea(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) throws RemoteException {
        modelUpdater.createPlayArea(nickname, cardPositions, visibleResources, freeSerializableCorners);
    }

    @Override
    public synchronized void placeCard(String nickname, String placedCardId, int row, int col) throws RemoteException {
        modelUpdater.placeCard(nickname, placedCardId, row, col);
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
    public synchronized void setBoardState(int currentTurn, GamePhase gamePhase) throws RemoteException {
        modelUpdater.setBoardState(currentTurn, gamePhase);
    }

    @Override
    public synchronized void playerDeadLockUpdate(String nickname, boolean isDeadLocked) throws RemoteException {
        modelUpdater.playerDeadLockUpdate(nickname, isDeadLocked);
    }

    @Override
    public void reportError(String errorMessage) throws RemoteException {
        modelUpdater.reportError(errorMessage);
    }

//endregion
}
