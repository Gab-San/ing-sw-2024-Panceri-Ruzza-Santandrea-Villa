package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.GameSubject;
import it.polimi.ingsw.model.listener.remote.errors.IllegalActionError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalParameterError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalStateError;
import it.polimi.ingsw.model.listener.remote.events.playerhand.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * This class represents the player hand, holding play cards and visible cards.
 */
public class PlayerHand implements GameSubject {
    private static final int MAX_CARDS = 3;
    private final List<PlayCard> cards;
    private final List<ObjectiveCard> secretObjective;
    private StartingCard startingCard;
    private int MAX_OBJECTIVES;
    private final Player playerRef;
    private final List<GameListener> gameListenerList;

    /**
     * Constructs a completely empty hand
     * @param playerRef the player who owns this hand
     */
    PlayerHand(Player playerRef){
        this.gameListenerList = new LinkedList<>();
        cards = new LinkedList<>();
        secretObjective = new ArrayList<>(2);
        startingCard = null;
        this.playerRef = playerRef;
        MAX_OBJECTIVES = 2;
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        if(other instanceof PlayerHand otherHand){
            return cards.equals(otherHand.cards) &&
                    secretObjective.equals(otherHand.secretObjective) &&
                    startingCard.equals(otherHand.startingCard);
        }
        else return false;
    }

    /**
     * Returns true if the card is in hand (comparing with ==), false otherwise.
     * @param card the card to look for in hand
     * @return true if the card is in hand (comparing with ==), false otherwise
     */
    public boolean containsCard(@NotNull PlayCard card) {
        for(PlayCard c : cards){
            if(c == card) return true;
        }
        return false;
    }

    /**
     * Returns the playable card at the specified index.
     * @param pos index (0-2)
     * @return card at index pos in this hand, without removing it from the hand
     * @throws IndexOutOfBoundsException if pos &lt; 0 or pos &gt;= hand size
     */
    public PlayCard getCard(int pos) throws IndexOutOfBoundsException{
        if (pos < 0 || pos >= cards.size()) throw new IndexOutOfBoundsException("Accessing illegal card index!");
        else return cards.get(pos);
    }

    /**
     * Returns the list of card in player's hand.
     * @return cards in player's hand
     */
    public List<PlayCard> peekCards() {
        return cards;
    }

    /**
     * Returns the playable card associated wit the specified id.
     * @param cardID ID of the requested card
     * @return card with given cardID in this hand, without removing it from the hand
     * @throws IllegalArgumentException if there is no card with given cardID in this hand
     */
    public PlayCard getCardByID(String cardID) throws IllegalArgumentException{
        return cards.stream()
                .filter(card -> card != null && cardID.equals(card.getCardID()))
                .findFirst().orElseThrow(()->{
                    notifyAllListeners(new IllegalParameterError(playerRef.getNickname(), "REQUESTED CARD IS NOT IN YOUR HAND"));
                    return new IllegalArgumentException("Player hand does not contain a card with ID " + cardID);
                });
    }
    /**
     * Returns the card at the specified position, removing it from the player's hand.
     * @param pos index (0-2)
     * @return card at index pos in this hand, removing it from the hand
     * @throws IndexOutOfBoundsException if pos &lt; 0 or pos &gt;= hand size
     */
    public PlayCard popCard(int pos) throws IndexOutOfBoundsException{
        if (pos < 0 || pos >= cards.size()) throw new IndexOutOfBoundsException("Accessing illegal card index!");
        else {
            PlayCard poppedCard = cards.remove(pos);
            notifyAllListeners(new PlayerHandRemoveCardEvent(playerRef.getNickname(), poppedCard));
            return poppedCard;
        }
    }

    /**
     * Adds a card to this hand
     * @param drawCard the card to add
     * @throws PlayerHandException if the hand is full or already contains the given card
     */
    public void addCard(@NotNull Supplier<PlayCard> drawCard) throws PlayerHandException{
        if(isHandFull()){
            notifyAllListeners(new IllegalActionError(playerRef.getNickname(), "Player hand is full. Can't draw".toUpperCase()));
            throw new PlayerHandException("PLAYER HAND IS FULL!", playerRef);
        }
        PlayCard drawnCard = drawCard.get();
        notifyAllListeners(new PlayerHandAddCardEvent(playerRef.getNickname(), drawnCard));
        cards.add(drawnCard);
    }

    /**
     * Returns true if the number of play cards in the player's hand is equal or higher than the maximum number
     * of cards a player can hold (3 by the rules), false otherwise.
     * @return true if a card cannot be added to the player's hand, false otherwise
     */
    public boolean isHandFull() {
        return cards.size() >= MAX_CARDS;
    }

    /**
     * Removes the given card to this hand
     * @param card the card to remove
     * @throws PlayerHandException if the hand does not contain the given card
     */
    public void removeCard(@NotNull PlayCard card) throws PlayerHandException{
        if(!containsCard(card)) throw new PlayerHandException("Card wasn't in hand!", playerRef, card.getClass());
        for (int i = 0; i < cards.size(); i++) {
            if(cards.get(i) == card) {
                PlayCard cardToRemove = cards.remove(i); // need this loop as cards.remove(card) would use the .equals method
                notifyAllListeners(new PlayerHandRemoveCardEvent(playerRef.getNickname(), cardToRemove));
                return;
            }
        }
    }

    /**
     * Returns the player's hand secret objective.
     * @return the selected secret objective of this hand
     * @throws PlayerHandException if the secret objective wasn't chosen yet
     */
    public ObjectiveCard getSecretObjective() throws PlayerHandException {
        if(MAX_OBJECTIVES == 2){
            notifyAllListeners(new IllegalStateError(playerRef.getNickname(), "Secret objective was not chosen yet.".toUpperCase()));
            throw new PlayerHandException("Secret objective was not chosen yet.", playerRef, ObjectiveCard.class);
        }
        return secretObjective.get(0); //.getFirst(); doesn't compile
    }

    /**
     * Adds an objective card to the hand
     * @param secretObjective the objective card to add
     * @throws PlayerHandException if two objective cards were already dealt or the objective card was already in this hand
     */
    public void addObjectiveCard(ObjectiveCard secretObjective) throws PlayerHandException {
        if(this.secretObjective.size() >= MAX_OBJECTIVES)
            throw new PlayerHandException("Objective cards were already dealt.", playerRef, ObjectiveCard.class);

        if(this.secretObjective.contains(secretObjective))
            throw new PlayerHandException("Trying to add duplicate secret objective", playerRef, ObjectiveCard.class);

        this.secretObjective.add(secretObjective);
        notifyAllListeners(new PlayerHandAddObjectiveCardEvent(playerRef.getNickname(), secretObjective));
    } 
    
    /**
     * Adds an objective card to the hand
     * @param drawCard the function that supplies the objective card to add
     * @throws PlayerHandException if two objective cards were already dealt or the objective card was already in this hand
     */
    public void addObjectiveCard(Supplier<ObjectiveCard> drawCard) throws PlayerHandException {
        if(this.secretObjective.size() >= MAX_OBJECTIVES)
            throw new PlayerHandException("Objective cards were already dealt.", playerRef, ObjectiveCard.class);
        ObjectiveCard objectiveCard = drawCard.get();
        this.secretObjective.add(objectiveCard);
        notifyAllListeners(new PlayerHandAddObjectiveCardEvent(playerRef.getNickname(), objectiveCard));
    }

    /**
     * Returns the secret cards currently held by the player.
     * @return player's secret cards
     */
    public List<ObjectiveCard> getObjectiveChoices() {
        return secretObjective;
    }

    /**
     * Removes the secret objective not chosen from the player's hand.
     * @param choice index of the chosen secret objective (1 or 2)
     * @throws IndexOutOfBoundsException if choice &lt;= 0 or choice &gt; 2
     * @throws PlayerHandException if secret objective was already chosen or if choices were never dealt
     */
    public void chooseObjective(int choice) throws IndexOutOfBoundsException, PlayerHandException{
        if(secretObjective.isEmpty()){
            notifyAllListeners(new IllegalStateError(playerRef.getNickname(),"Objective choices not initialized.".toUpperCase()));
            throw new PlayerHandException("Objective choices not initialized.", playerRef, ObjectiveCard.class);
        }
        if(MAX_OBJECTIVES == 1){
            notifyAllListeners(new IllegalStateError(playerRef.getNickname(),"Secret objective was already chosen.".toUpperCase()));
            throw new PlayerHandException("Secret objective was already chosen.", playerRef, ObjectiveCard.class);
        }

        try {
            secretObjective.remove(2 - choice); // 2-choice == the index that was not chosen
        } catch (IndexOutOfBoundsException e){
            notifyAllListeners(new IllegalParameterError(playerRef.getNickname(), "Invalid choice. Must be 1 or 2, entered " + choice + " instead.".toUpperCase()));
            throw e;
        }
        MAX_OBJECTIVES = 1;
        notifyAllListeners(new PlayerHandChooseObjectiveCardEvent(playerRef.getNickname(), getSecretObjective()));
    }

    /**
     * Returns the starting card.
     * @return this hand's starting card
     * @throws PlayerHandException if starting card was not dealt
     */
    public StartingCard getStartingCard() throws PlayerHandException {
        if(startingCard == null)
            throw new PlayerHandException("Starting card was not dealt before trying to access it.", playerRef, StartingCard.class);
        return startingCard;
    }

    /**
     * Returns the current starting card (may be null).
     * @return current starting card (if owned)
     */
    public StartingCard peekStartingCard(){
        return startingCard;
    }
    /**
     * Sets this hand's starting card
     * @param startingCard the starting card
     * @throws PlayerHandException if the starting card was already dealt
     */
    public void setStartingCard(StartingCard startingCard) throws PlayerHandException {
        if(this.startingCard != null){
            throw new PlayerHandException("Starting Card already set", playerRef, StartingCard.class);
        }
        this.startingCard = startingCard;
        notifyAllListeners(new PlayerHandSetStartingCardEvent(playerRef.getNickname(), startingCard));
    }
    
    /**
     * Sets this hand's starting card only if it isn't set already
     * @param drawCard the function the draws the startingCard
     * @throws PlayerHandException if the starting card was already dealt
     */
    public void setStartingCard(Supplier<StartingCard> drawCard) throws PlayerHandException {
        if(this.startingCard != null){
            throw new PlayerHandException("Starting Card already set", playerRef, StartingCard.class);
        }
        this.startingCard = drawCard.get();
        notifyAllListeners(new PlayerHandSetStartingCardEvent(playerRef.getNickname(), startingCard));
    }

    /**
     * Returns the associated player instance.
     * @return associated player
     */
    public Player getPlayerRef(){
        return playerRef;
    }


    @Override
    public void addListener(GameListener listener) {
        synchronized (gameListenerList) {
            gameListenerList.add(listener);
        }
    }

    @Override
    public void removeListener(GameListener listener) {
        synchronized (gameListenerList) {
            gameListenerList.remove(listener);
        }
    }

    @Override
    public void notifyAllListeners(GameEvent event) throws ListenException {
        synchronized (gameListenerList) {
            for (GameListener listener : gameListenerList) {
                listener.listen(event);
            }
        }
    }
}

