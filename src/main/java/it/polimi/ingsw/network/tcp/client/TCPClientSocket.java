package it.polimi.ingsw.network.tcp.client;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.TCPMessage;
import it.polimi.ingsw.network.tcp.message.TCPServerCheckMessage;
import it.polimi.ingsw.network.tcp.message.TCPServerMessage;
import it.polimi.ingsw.view.ModelUpdater;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
    }
//region SOCKET THREADS

    private void startUpdateExecutor(){
        new Thread(
                () -> {
                    while (!clientSocket.isClosed()){
                        TCPServerMessage update;
                        synchronized (updateQueue) {
                            while (updateQueue.isEmpty()) {
                                try {
                                    updateQueue.wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            update = updateQueue.remove();
                        }
                        try {
                            update.execute(this);
                        } catch (RemoteException e) {
                            System.err.println(e.getMessage() + "\n" + e.getCause().getMessage());
//                            closeSocket();
                        }
                    }
                }
        ).start();
    }


    public void startReader() {
        new Thread(
                () -> {
//                    System.out.println("Socket connection started!");
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
                    } catch(EOFException eofException) {
                        System.out.println("REACHED EOS!");
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

    public void setModelUpdater(ModelUpdater modelUpdater){
        this.modelUpdater = modelUpdater;
        startUpdateExecutor();
    }
//endregion

//region VIRTUAL CLIENT INTERFACE
    @Override
    public void displayMessage(String messenger, String msg) throws RemoteException{
        modelUpdater.displayMessage(messenger, msg);
    }

    @Override
    public void ping() throws RemoteException {
        return;
    }


    /**
     * Notifies about the board initialization status
     *
     * @param currentTurn    the board current turn at initialization
     * @param scoreboard     current scoreboard state
     * @param gamePhase      the game phase at initialization
     * @param playerDeadLock current player dead locks
     * @throws RemoteException if a connection error is detected
     */
    @Override
    public void setBoardState(int currentTurn, Map<String, Integer> scoreboard, GamePhase gamePhase, Map<String, Boolean> playerDeadLock) throws RemoteException {
        modelUpdater.setBoardState(currentTurn, scoreboard, gamePhase, playerDeadLock);
    }

    @Override
    public void notifyEndgame() throws RemoteException {
        modelUpdater.notifyEndgame();
    }

    @Override
    public void notifyEndgame(String nickname, int score) throws RemoteException {
        modelUpdater.notifyEndgame(nickname, score);
    }


    @Override
    public void removePlayer(String nickname) throws RemoteException {
         modelUpdater.removePlayer(nickname);
    }

    /**
     * Notifies a change in the player color
     *
     * @param nickname the unique nickname identifier of the player
     * @param colour   the colour chosen from the player for the match
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void updatePlayer(String nickname, PlayerColor colour) throws RemoteException {
        modelUpdater.updatePlayer(nickname, colour);
    }

    /**
     * Updates the player turn
     *
     * @param nickname   the unique nickname identifier of the player
     * @param playerTurn the turn randomly given to the player
     * @throws RemoteException if a connection error is detected
     */
    @Override
    public void updatePlayer(String nickname, int playerTurn) throws RemoteException {
        modelUpdater.updatePlayer(nickname, playerTurn);
    }

    /**
     * Updates the connection status of the player
     *
     * @param nickname    the unique nickname identifier of the player
     * @param isConnected the current connection status of the player
     * @throws RemoteException if a connection error is detected
     */
    @Override
    public void updatePlayer(String nickname, boolean isConnected) throws RemoteException {
        modelUpdater.updatePlayer(nickname, isConnected);
    }

    /**
     * Notifies about the current state of the player.
     *
     * @param nickname    the unique nickname identifier of the player
     * @param isConnected the connection status as for the moment of the displayMessage
     * @param turn        the given player's turn
     * @param colour      the colour the player has chosen for the match
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {
        modelUpdater.setPlayerState(nickname, isConnected, turn ,colour);
    }

    /**
     * Notifies a change in the specified deck.
     * <p>
     * A deck has three positions to model the three visible cards of a deck: <br>
     * 0 - the top card <br>
     * 1 - the first revealed card <br>
     * 2 - the second revealed card <br>
     * <br>
     * A revealed card is one of the previous, as once a card is drawn another one must be revealed.
     * </p>
     *
     * @param deck         the changed deck identifier
     * @param revealedId   the identifier of the revealed card
     * @param cardPosition the card which was changed (top = 0, first = 1, second = 2)
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void deckUpdate(char deck, String revealedId, int cardPosition) throws RemoteException {
        modelUpdater.deckUpdate(deck, revealedId, cardPosition);
    }

    /**
     * Notifies about the current state of the deck.
     * <p>
     * Since all three of the cards are given it means that at least all of the
     * position of the deck have a drawable card.
     * <br>
     * It doesn't give any information about the status of the rest of the deck.
     * </p>
     *
     * @param deck     the deck identifier
     * @param topId    the top card currently visible on the deck
     * @param firstId  the first revealed card of the deck
     * @param secondId the second revealed card of the deck
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void setDeckState(char deck, String topId, String firstId, String secondId) throws RemoteException {
        modelUpdater.setDeckState(deck, topId, firstId, secondId);
    }

    /**
     * Notifies about the current state of the deck.
     * <p>
     * This displayMessage can be triggered iff in the deck remained just one card.
     * <br>
     * The card can be only one of the revealed ones, due to the decks' rule that obliges
     * to reveal a card after a revealed card has been drawn. This implies that cardPosition will
     * only accept values 1 or 2.
     * </p>
     *
     * @param deck         the deck identifier
     * @param revealedId   the only revealed card remained in the deck
     * @param cardPosition the revealed card position
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void setDeckState(char deck, String revealedId, int cardPosition) throws RemoteException {
        modelUpdater.setDeckState(deck, revealedId, cardPosition);
    }

    /**
     * Notifies about the current state of the identified deck.
     * <p>
     * This displayMessage can be triggered iff the deck's face-down card pile is empty
     * thus displaying only the two revealed cards.
     * </p>
     *
     * @param deck         the deck identifier
     * @param firstCardId  the first card identifier
     * @param secondCardId the second card identifier
     * @throws RemoteException if a connection error is detected
     */
    @Override
    public void setDeckState(char deck, String firstCardId, String secondCardId) throws RemoteException {
        modelUpdater.setDeckState(deck, firstCardId, secondCardId);
    }

    /**
     * Updates the current revealed card's position to empty
     *
     * @param deck         the deck identifier
     * @param cardPosition the empty revealed card's position (1 or 2)
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void emptyReveal(char deck, int cardPosition) throws RemoteException {
        modelUpdater.emptyReveal(deck, cardPosition);
    }

    /**
     * Updates the current deck's face-down pile to empty
     *
     * @param deck the deck identifier
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void emptyFaceDownPile(char deck) throws RemoteException {
        modelUpdater.emptyFaceDownPile(deck);
    }

    /**
     * Updates the current state of a deck.
     * <p>
     * This displayMessage can be triggered iff a deck is empty during initialization.
     * </p>
     *
     * @param deck the deck identifier
     * @throws RemoteException if a connection error is detected
     */
    @Override
    public void setEmptyDeckState(char deck) throws RemoteException {
        modelUpdater.setEmptyDeckState(deck);
    }

    /**
     * Notifies about game phase change in match
     *
     * @param gamePhase current game phase
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void updatePhase(GamePhase gamePhase) throws RemoteException {
        modelUpdater.updatePhase(gamePhase);
    }

    /**
     * Notifies about player's score change
     *
     * @param nickname the unique player's identifier whose score has changed
     * @param score    the player's score change
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void updateScore(String nickname, int score) throws RemoteException {
        modelUpdater.updateScore(nickname, score);
    }

    /**
     * Updates current match turn
     *
     * @param currentTurn the match's current turn
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void updateTurn(int currentTurn) throws RemoteException {
        modelUpdater.updateTurn(currentTurn);
    }

    /**
     * Notifies about the current player's hand status
     *
     * @param nickname       the unique player's identifier
     * @param playCards      the list of playable cards in the player's hand (can be empty)
     * @param objectiveCards the list of objective cards in the player's hand (can be empty)
     * @param startingCard   the starting card in the player's hand (may be null)
     * @throws RemoteException if a connection error is detected
     */
    @Override
    public void setPlayerHandState(String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) throws RemoteException {
        modelUpdater.setPlayerHandState(nickname, playCards, objectiveCards, startingCard);
    }

    /**
     * Updates the current player's hand status after a card was added to it
     *
     * @param nickname    the unique player's identifier
     * @param drawnCardId the id of the card added in the hand
     * @throws RemoteException if a connection error is detected
     */
    @Override
    public void playerHandAddedCardUpdate(String nickname, String drawnCardId) throws RemoteException {
        modelUpdater.playerHandAddedCardUpdate(nickname, drawnCardId);
    }

    /**
     * Updates the current player's hand status after a card was removed
     *
     * @param nickname   the unique player's id
     * @param playCardId the played card identifier
     * @throws RemoteException if a connection error is detected
     */
    @Override
    public void playerHandRemoveCard(String nickname, String playCardId) throws RemoteException {
            modelUpdater.playerHandRemoveCard(nickname, playCardId);
    }

    /**
     * Updates the current player's hand status after an objective card was drawn
     *
     * @param nickname      the unique player's identifier
     * @param objectiveCard the added objective card's id
     * @throws RemoteException if connection was lost
     */
    @Override
    public void playerHandAddObjective(String nickname, String objectiveCard) throws RemoteException {
        modelUpdater.playerHandAddObjective(nickname, objectiveCard);
    }

    /**
     * Updates the current player's hand after an objective card was chosen
     *
     * @param nickname          player's id
     * @param chosenObjectiveId the id of the chosen objective card
     * @throws RemoteException if connection was lost
     */
    @Override
    public void playerHandChooseObject(String nickname, String chosenObjectiveId) throws RemoteException {
        modelUpdater.playerHandChooseObject(nickname, chosenObjectiveId);
    }

    /**
     * Updates the current player's hand after the starting card was dealt
     *
     * @param nickname       player's id
     * @param startingCardId the id of the given starting card
     * @throws RemoteException if connection was lost
     */
    @Override
    public void playerHandSetStartingCard(String nickname, String startingCardId) throws RemoteException {
        modelUpdater.playerHandSetStartingCard(nickname, startingCardId);
    }


    /**
     * Notifies about the player's play area status
     *
     * @param nickname                the play area owner's id
     * @param cardPositions           a list/set of card positions representing the play area (can be empty)
     * @param visibleResources        a map of the current visible resources on the player's play area (can be empty)
     * @param freeSerializableCorners a representation of the free corners in the player's play area (can be empty)
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void setPlayAreaState(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) throws RemoteException {
        modelUpdater.setPlayAreaState(nickname, cardPositions, visibleResources, freeSerializableCorners);
    }

    /**
     * Notifies about card's placement.
     * <p>
     * The parameter (0,0) is strictly associated to the starting card. <br>
     * Therefore if this parameter is read when a non-starting card is passed that
     * an error must occur.
     * </p>
     *
     * @param nickname     the play area owner's identifier
     * @param placedCardId the placed card's identifier
     * @param row          the row in which the card was placed
     * @param col          the column in which the card was placed
     * @param placeOnFront the side on which to place the card
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void updatePlaceCard(String nickname, String placedCardId, int row, int col, boolean placeOnFront) throws RemoteException {
        modelUpdater.updatePlaceCard(nickname, placedCardId, row, col, placeOnFront);
    }

    /**
     * Updates the visible resources map
     *
     * @param nickname         the play area owner's id
     * @param visibleResources the current visible resources in the play area
     * @throws RemoteException if an error during connection is detected
     */
    @Override
    public void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) throws RemoteException {
        modelUpdater.visibleResourcesUpdate(nickname, visibleResources);
    }

    /**
     * The list of current free corners represented so that they are serializable
     *
     * @param nickname                the play area owner's id
     * @param freeSerializableCorners the current list of free corners
     * @throws RemoteException if an error during connection occurs
     */
    @Override
    public void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerializableCorners) throws RemoteException {
        modelUpdater.freeCornersUpdate(nickname, freeSerializableCorners);
    }

    /**
     * Updates the player's deadlock status
     * <p>
     * Complete with dead lock description
     * </p>
     *
     * @param nickname     the player unique id
     * @param isDeadLocked the player deadlock value
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void playerDeadLockUpdate(String nickname, boolean isDeadLocked) throws RemoteException {
        modelUpdater.playerDeadLockUpdate(nickname, isDeadLocked);
    }

    /**
     * Reports an error occurred while evaluating player action.
     *
     * @param errorMessage the message related to the triggered error
     * @throws RemoteException if an error during connection is detected
     */
    @Override
    public void reportError(String errorMessage) throws RemoteException {
        modelUpdater.reportError(errorMessage);
    }

    @Override
    public void notifyIndirectDisconnect() throws RemoteException {
        modelUpdater.notifyIndirectDisconnect();
    }

//endregion
}
