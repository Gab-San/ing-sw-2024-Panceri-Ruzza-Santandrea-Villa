package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.enums.PlayerColor;
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

    }

    @Override
    public void notifyEndgame() throws RemoteException {

    }

    @Override
    public void notifyEndgame(String nickname, int score) throws RemoteException {

    }

    @Override
    public void removePlayer(String nickname) throws RemoteException {

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

    }

    /**
     * Notifies about the current state of the player.
     *
     * @param nickname    the unique nickname identifier of the player
     * @param isConnected the connection status as for the moment of the update
     * @param turn        the given player's turn
     * @param colour      the colour the player has chosen for the match
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {

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

    }

    /**
     * Notifies about the current state of the deck.
     * <p>
     * This update can be triggered iff in the deck remained just one card.
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

    }

    /**
     * Notifies about the current state of the identified deck.
     * <p>
     * This update can be triggered iff the deck's face-down card pile is empty
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

    }


    /**
     * Updates the current deck's face-down pile to empty
     *
     * @param deck the deck identifier
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void emptyFaceDownPile(char deck) throws RemoteException {

    }

    /**
     * Updates the current state of a deck.
     * <p>
     * This update can be triggered iff a deck is empty during initialization.
     * </p>
     *
     * @param deck the deck identifier
     * @throws RemoteException if a connection error is detected
     */
    @Override
    public void setEmptyDeckState(char deck) throws RemoteException {

    }

    /**
     * Notifies about game phase change in match
     *
     * @param gamePhase current game phase
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void updatePhase(GamePhase gamePhase) throws RemoteException {

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

    }

    /**
     * Updates current match turn
     *
     * @param currentTurn the match's current turn
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void updateTurn(int currentTurn) throws RemoteException {

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
    public void createPlayArea(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) throws RemoteException {

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
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void placeCard(String nickname, String placedCardId, int row, int col) throws RemoteException {

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

    }

    /**
     * Reports an error occurred while evaluating player action.
     *
     * @param errorMessage the message related to the triggered error
     * @throws RemoteException if an error during connection is detected
     */
    @Override
    public void reportError(String errorMessage) throws RemoteException {

    }

    @Override
    public void notifyTimeoutDisconnect() throws RemoteException{

    }


    public RMIServerProxy getProxy(){
        return proxy;
    }
}
