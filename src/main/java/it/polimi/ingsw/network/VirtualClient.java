package it.polimi.ingsw.network;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.CardPosition;
import it.polimi.ingsw.SerializableCorner;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * This interface represents the skeletal structure of a client
 */
public interface VirtualClient extends Remote, Serializable {
    /**
     * The UID required for serialization.
     */
    long serialVersionUID =-124819284198L;

    /**
     * Displays chat messages.
     *
     * @param messenger message messenger
     * @param msg       message to display
     * @throws RemoteException if an error occurs during connection
     */
    void displayMessage(String messenger, String msg) throws RemoteException;

    /**
     * Pings the remote end of the connection, testing if reachable.
     * @throws RemoteException if an error occurs during connection
     */
    void ping() throws RemoteException;

    /**
     * Notifies about the board initialization status.
     * @param currentTurn the board current turn at initialization
     * @param gamePhase the game phase at initialization
     * @param scoreboard current scoreboard state
     * @param playerDeadLock current player deadlocks
     * @throws RemoteException if a connection error is detected
     */
    void setBoardState(int currentTurn, Map<String, Integer> scoreboard, GamePhase gamePhase, Map<String, Boolean> playerDeadLock) throws RemoteException;

    /**
     * Notifies about game phase change in match.
     * @param gamePhase current game phase
     * @throws RemoteException if a connection error occurs
     */
    void updatePhase(GamePhase gamePhase) throws RemoteException;

    /**
     * Notifies about player's score change.
     * @param nickname the unique player's identifier whose score has changed
     * @param score the player's score change
     * @throws RemoteException if a connection error occurs
     */
    void updateScore(String nickname, int score) throws RemoteException;

    /**
     * Updates current match turn.
     * @param currentTurn the match's current turn
     * @throws RemoteException if a connection error occurs
     */
    void updateTurn(int currentTurn) throws RemoteException;

    /**
     * Updates the player's deadlock status.
     * <p>
     *     Complete with dead lock description.
     * </p>
     * @param nickname the player unique id
     * @param isDeadLocked the player deadlock value
     * @throws RemoteException if a connection error occurs
     */
    void playerDeadLockUpdate(String nickname, boolean isDeadLocked) throws RemoteException;

    /**
     * Notifies the start of the endgame due to decks emptying.
     * @throws RemoteException if an error occurs while send notification
     */
    void notifyEndgame() throws RemoteException;

    /**
     * Notifies the start of the endgame because the highlighted player has reached or surpassed
     * the score marker of 20.
     * @param nickname the unique player's nickname identifier
     * @param score the score with which he triggered the endgame
     * @throws RemoteException if a connection error occurs
     */
    void notifyEndgame(String nickname, int score) throws RemoteException;


    /**
     * Notifies about the current state of the player.
     * @param nickname the unique nickname identifier of the player
     * @param isConnected the connection status as for the moment of the displayMessage
     * @param turn the given player's turn
     * @param colour the colour the player has chosen for the match
     * @throws RemoteException if a connection error occurs
     */
    void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException;


    /**
     * Notifies a change in the player color.
     * @param nickname the unique nickname identifier of the player
     * @param colour the colour chosen from the player for the match
     * @throws RemoteException if a connection error occurs
     */
    void updatePlayer(String nickname, PlayerColor colour) throws RemoteException;

    /**
     * Updates the player turn.
     * @param nickname the unique nickname identifier of the player
     * @param playerTurn the turn randomly given to the player
     * @throws RemoteException if a connection error is detected
     */
    void updatePlayer(String nickname, int playerTurn) throws RemoteException;

    /**
     * Updates the connection status of an opponent.
     * @param nickname the unique nickname identifier of the opponent
     * @param isConnected the current connection status of the opponent
     * @throws RemoteException if a connection error is detected
     */
    void updatePlayer(String nickname, boolean isConnected) throws RemoteException;

    /**
     * Notifies about the removal of an opponent.
     * @param nickname the opponent's unique nickname identifier
     * @throws RemoteException if a connection error is detected
     */
    void removePlayer(String nickname) throws RemoteException;

    /**
     * Notifies about the current state of the deck.
     *<p>
     * Since all three of the cards are given it means that at least all of the
     * position of the deck have a drawable card.
     *<br>
     * It doesn't give any information about the status of the rest of the deck.
     * </p>
     * @param deck the deck identifier
     * @param topId the top card currently visible on the deck
     * @param firstId the first revealed card of the deck
     * @param secondId the second revealed card of the deck
     * @throws RemoteException if a connection error occurs
     */
    void setDeckState(char deck, String topId, String firstId, String secondId) throws RemoteException;

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
     * @param deck the deck identifier
     * @param revealedId the only revealed card remained in the deck
     * @param cardPosition the revealed card position
     * @throws RemoteException if a connection error occurs
     */
    void setDeckState(char deck, String revealedId, int cardPosition) throws RemoteException;

    /**
     * Notifies about the current state of the identified deck.
     * <p>
     *     This displayMessage can be triggered iff the deck's face-down card pile is empty
     *     thus displaying only the two revealed cards.
     * </p>
     * @param deck the deck identifier
     * @param firstCardId the first card identifier
     * @param secondCardId the second card identifier
     * @throws RemoteException if a connection error is detected
     */
    void setDeckState(char deck, String firstCardId,
                      String secondCardId) throws RemoteException;

    /**
     * Updates the current state of a deck.
     * <p>
     *     This displayMessage can be triggered iff a deck is empty during initialization.
     * </p>
     * @param deck the deck identifier
     * @throws RemoteException if a connection error is detected
     */
    void setEmptyDeckState(char deck) throws RemoteException;

    /**
     * Notifies a change in the specified deck.
     *<p>
     * A deck has three positions to model the three visible cards of a deck: <br>
     * 0 - the top card; <br>
     * 1 - the first revealed card;<br>
     * 2 - the second revealed card. <br>
     *<br>
     * A revealed card is one of the previous, as once a card is drawn another one must be revealed.
     * </p>
     * @param deck the changed deck identifier
     * @param revealedId the identifier of the revealed card
     * @param cardPosition the card which was changed (top = 0, first = 1, second = 2)
     * @throws RemoteException if a connection error occurs
     */
    void deckUpdate(char deck, String revealedId, int cardPosition) throws RemoteException;


    /**
     * Updates the current revealed card's position to empty.
     * @param deck the deck identifier
     * @param cardPosition the empty revealed card's position (1 or 2)
     * @throws RemoteException if a connection error occurs
     */
    void emptyReveal(char deck, int cardPosition) throws RemoteException;

    /**
     * Updates the current deck's face-down pile to empty.
     * @param deck the deck identifier
     * @throws RemoteException if a connection error occurs
     */
    void emptyFaceDownPile(char deck) throws RemoteException;



    /**
     * Notifies about the current player's hand status.
     * @param nickname the unique player's identifier
     * @param playCards the list of playable cards in the player's hand (can be empty)
     * @param objectiveCards the list of objective cards in the player's hand (can be empty)
     * @param startingCard the starting card in the player's hand (may be null)
     * @throws RemoteException if a connection error is detected
     */
    void setPlayerHandState(String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) throws RemoteException;

    /**
     * Updates the current player's hand status after a card was added to it.
     * @param nickname the unique player's identifier
     * @param drawnCardId the id of the card added in the hand
     * @throws RemoteException if a connection error is detected
     */
    void playerHandAddedCardUpdate(String nickname, String drawnCardId) throws RemoteException;

    /**
     * Updates the current player's hand status after a card was removed.
     * @param nickname the unique player's id
     * @param playCardId the played card identifier
     * @throws RemoteException if a connection error is detected
     */
    void playerHandRemoveCard(String nickname, String playCardId) throws RemoteException;

    /**
     * Updates the current player's hand status after an objective card was drawn.
     * @param nickname the unique player's identifier
     * @param objectiveCard the added objective card's id
     * @throws RemoteException if connection was lost
     */
    void playerHandAddObjective(String nickname, String objectiveCard) throws RemoteException;

    /**
     * Updates the current player's hand after an objective card was chosen.
     * @param nickname player's id
     * @param chosenObjectiveId the id of the chosen objective card
     * @throws RemoteException if connection was lost
     */
    void playerHandChooseObject(String nickname, String chosenObjectiveId) throws RemoteException;

    /**
     * Updates the current player's hand after the starting card was dealt.
     * @param nickname player's id
     * @param startingCardId the id of the given starting card
     * @throws RemoteException if connection was lost
     */
    void playerHandSetStartingCard(String nickname, String startingCardId) throws RemoteException;

    /**
     * Notifies about the player's play area status.
     * @param nickname the play area owner's id
     * @param cardPositions a list/set of card positions representing the play area (can be empty)
     * @param visibleResources a map of the current visible resources on the player's play area (can be empty)
     * @param freeSerializableCorners a representation of the free corners in the player's play area (can be empty)
     * @throws RemoteException if a connection error occurs
     */
    void setPlayAreaState(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) throws RemoteException;

    /**
     * Notifies about card's placement.
     * <p>
     *     The parameter (0,0) is strictly associated to the starting card. <br>
     *     Therefore if this parameter is read when a non-starting card is passed that
     *     an error must occur.
     * </p>
     *
     * @param nickname     the play area owner's identifier
     * @param placedCardId the placed card's identifier
     * @param row          the row in which the card was placed
     * @param col          the column in which the card was placed
     * @param placeOnFront the side on which to place the card
     * @throws RemoteException if a connection error occurs
     */
    void updatePlaceCard(String nickname, String placedCardId, int row, int col, boolean placeOnFront) throws RemoteException;

    /**
     * Updates the visible resources map.
     * @param nickname the play area owner's id
     * @param visibleResources the current visible resources in the play area
     * @throws RemoteException if an error during connection is detected
     */
    void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) throws RemoteException;

    /**
     * The list of current free corners represented so that they are serializable.
     * @param nickname the play area owner's id
     * @param freeSerializableCorners the current list of free corners
     * @throws RemoteException if an error during connection occurs
     */
    void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerializableCorners) throws RemoteException;

    /**
     * Reports an error occurred while evaluating player action.
     * @param errorMessage the message related to the triggered error
     * @throws RemoteException if an error during connection is detected
     */
    void reportError(String errorMessage) throws RemoteException;

    /**
     * Reports an indirect disconnection notification.
     * @throws RemoteException if an error during connection is detected
     */
    void notifyIndirectDisconnect() throws RemoteException;
}
