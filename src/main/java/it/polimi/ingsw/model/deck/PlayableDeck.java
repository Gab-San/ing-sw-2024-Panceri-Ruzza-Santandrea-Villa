package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.deck.cardfactory.CardFactory;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.GameSubject;
import it.polimi.ingsw.model.listener.remote.events.deck.DeckRevealEvent;
import it.polimi.ingsw.model.listener.remote.events.deck.FaceDownReplaceEvent;
import it.polimi.ingsw.network.CentralServer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the deck of playable cards.
 */
public class PlayableDeck implements GameSubject {
    private final Deque<PlayCard> cardDeck;
    private final CardFactory cardFactory;
    private PlayCard firstRevealedCard;
    private PlayCard secondRevealedCard;
    private final char deckType;
    /**
     * Top card's position integer value.
     */
    public static final int TOP_POSITION = 0;
    /**
     * First revealed card's position integer value.
     */
    public static final int FIRST_POSITION = 1;
    /**
     * Second card's position integer value.
     */
    public static final int SECOND_POSITION = 2;
    private final List<GameListener> gameListenerList;

    /**
     * Constructs playable deck of the specified type, with the specified initial capacity.
     * Deck type and card factory type must be equal.
     * @param deckType deck type identifier
     * @param cardFactory deck type card factory
     * @param initialCapacity initial deck capacity
     */
    public PlayableDeck(char deckType, CardFactory cardFactory, int initialCapacity) {
        this.deckType = deckType;
        cardDeck = new ArrayDeque<>(initialCapacity);
        gameListenerList = new LinkedList<>();
        this.cardFactory = cardFactory;
        fillDeck(initialCapacity);
        reveal(FIRST_POSITION);
        reveal(SECOND_POSITION);
    }

    private void fillDeck(int initialCapacity){
        for(int i = 0; i < initialCapacity ; i++ ){
            try {
                cardDeck.add(cardFactory.addCardToDeck());
            } catch (DeckException ignore){}
        }
    }

    /**
     * Pops the top card of the face-down pile of the deck and replaces it with the next one.
     * @return top card of the deck
     */
    public synchronized PlayCard getTopCard(){
        PlayCard returnCard;
        returnCard = cardDeck.poll();
        try {
            if(!CentralServer.isEmptyDeckMode()) {
                cardDeck.add(cardFactory.addCardToDeck());
            }
        } catch (DeckException ignored){
            // If the cardFactory is empty there's no mean to throw an error
        }
        notifyAllListeners(new FaceDownReplaceEvent(deckType, cardDeck.peek()));
        return returnCard;
    }

    /**
     * Returns the top card of the face down card pile.
     * @return top card of the deck
     */
    public synchronized PlayCard peekTop(){
        return cardDeck.peek();
    }

    /**
     * Pops the first revealed card and replaces it with the top card of the face down pile.
     * @return first revealed card
     */
    public synchronized PlayCard getFirstRevealedCard(){
        PlayCard returnCard = firstRevealedCard;
        reveal(FIRST_POSITION);
        return returnCard;
    }

    /**
     * Returns the first revealed card of the deck.
     * @return first revealed card
     */
    public synchronized PlayCard peekFirst(){
        return firstRevealedCard;
    }
    /**
     * Pops the second revealed card and replaces it with the top card of the face down pile.
     * @return second revealed card
     */
    public synchronized PlayCard getSecondRevealedCard(){
        PlayCard returnCard = secondRevealedCard;
        reveal(SECOND_POSITION);
        return returnCard;
    }

    /**
     * Returns the second revealed card of the deck.
     * @return second revealed card of teh deck
     */
    public synchronized PlayCard peekSecond(){
        return secondRevealedCard;
    }

    private void reveal(int position){
        switch(position){
            case FIRST_POSITION:
                firstRevealedCard = getTopCard();
                notifyAllListeners(new DeckRevealEvent(deckType, firstRevealedCard, FIRST_POSITION));
                break;
            case SECOND_POSITION:
                secondRevealedCard = getTopCard();
                notifyAllListeners(new DeckRevealEvent(deckType, secondRevealedCard, SECOND_POSITION));
                break;
            default:
                break;
        }
    }

    /**
     * Returns true if the face-down pile of the deck is empty, false otherwise.
     * @return true if the face-down pile of the deck is empty, false otherwise
     */
    public synchronized boolean isEmpty(){
        return cardDeck.isEmpty();
    }

    /**
     * Returns true if the face-down pile of the deck is empty and both
     * the revealed cards positions are empty, false otherwise
     * @return true if there are no more drawable cards, false otherwise
     */
    public synchronized boolean isCompletelyEmpty(){
        return isEmpty() && isSecondRevealedEmpty() && isFirstRevealedEmpty();
    }

    /**
     * Returns true if the first revealed card position is empty, false otherwise.
     * @return true if the first revealed card position is empty, false otherwise
     */
    public boolean isFirstRevealedEmpty() {
        return firstRevealedCard == null;
    }

    /**
     * Returns true if the second revealed card position is empty, false otherwise.
     * @return true if the second revealed card position is empty, false otherwise
     */
    public boolean isSecondRevealedEmpty() {
        return secondRevealedCard == null;
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
