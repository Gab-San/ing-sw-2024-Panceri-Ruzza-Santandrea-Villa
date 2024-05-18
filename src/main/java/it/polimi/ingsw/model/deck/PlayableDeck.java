package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.GameSubject;
import it.polimi.ingsw.model.listener.remote.events.deck.DeckRevealEvent;
import it.polimi.ingsw.model.listener.remote.events.deck.DeckStateUpdateEvent;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.deck.cardfactory.CardFactory;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.listener.remote.events.deck.DrawnCardEvent;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class PlayableDeck implements GameSubject {
    private final Deque<PlayCard> cardDeck;
    private final CardFactory cardFactory;
    private PlayCard firstRevealedCard;
    private PlayCard secondRevealedCard;
    private final char deckType;

    public static final int FIRST_POSITION = 1;
    public static final int SECOND_POSITION = 2;
    private final List<GameListener> gameListenerList;


    public PlayableDeck(char deckType, CardFactory cardFactory, int initialCapacity) throws DeckInstantiationException {
        this.deckType = deckType;
        cardDeck = new ArrayDeque<>(initialCapacity);
        gameListenerList = new LinkedList<>();
        this.cardFactory = cardFactory;
        try {
            fillDeck(initialCapacity);
        } catch (DeckException e) {
            throw new DeckInstantiationException("Error while filling deck");
        }
        reveal(FIRST_POSITION);
        reveal(SECOND_POSITION);
    }

    private void fillDeck(int initialCapacity) throws DeckException {
        for(int i = 0; i < initialCapacity; i++ ){
            cardDeck.add(cardFactory.addCardToDeck());
        }
    }

    public synchronized PlayCard getTopCard(){
        PlayCard returnCard;
        returnCard = cardDeck.remove();
        try {
            cardDeck.add(cardFactory.addCardToDeck());
        } catch (DeckException ignored){
            // If the cardFactory is empty there's no mean to throw an error
        }
        notifyAllListeners(new DrawnCardEvent(deckType, cardDeck.peek()));
        return returnCard;
    }

    public synchronized PlayCard getFirstRevealedCard(){
        PlayCard returnCard = firstRevealedCard;
        reveal(FIRST_POSITION);
        return returnCard;
    }

    public synchronized PlayCard getSecondRevealedCard(){
        PlayCard returnCard = secondRevealedCard;
        reveal(SECOND_POSITION);
        return returnCard;
    }


    private void reveal(int position){
        switch(position){
            case FIRST_POSITION:
                if(cardDeck.isEmpty()) {
                    firstRevealedCard = null;
                    notifyAllListeners(new DeckRevealEvent(deckType, null, FIRST_POSITION));
                    break;
                }
                firstRevealedCard = getTopCard();
                notifyAllListeners(new DeckRevealEvent(deckType, firstRevealedCard, FIRST_POSITION));
                break;
            case SECOND_POSITION:
                if(cardDeck.isEmpty()) {
                    secondRevealedCard = null;
                    notifyAllListeners(new DeckRevealEvent(deckType, null, FIRST_POSITION));
                    break;
                }
                secondRevealedCard = getTopCard();
                notifyAllListeners(new DeckRevealEvent(deckType, firstRevealedCard, FIRST_POSITION));
                break;
            default:
                break;
        }
    }

    public synchronized boolean isEmpty(){
        return cardDeck.isEmpty();
    }
    public synchronized boolean isCompletelyEmpty(){
        return isEmpty() && isSecondRevealedEmpty() && isFirstRevealedEmpty();
    }

    public boolean isFirstRevealedEmpty() {
        return firstRevealedCard == null;
    }

    public boolean isSecondRevealedEmpty() {
        return secondRevealedCard == null;
    }

    @Override
    public void addListener(GameListener listener) {
        gameListenerList.add(listener);
        notifyListener(listener, new DeckStateUpdateEvent(deckType, cardDeck.peek(), firstRevealedCard, secondRevealedCard));
    }

    @Override
    public void removeListener(GameListener listener) {
        gameListenerList.remove(listener);
    }

    @Override
    public void notifyAllListeners(GameEvent event) {
        for(GameListener listener: gameListenerList){
            listener.listen(event);
        }
    }

    @Override
    public void notifyListener(GameListener listener, GameEvent event) throws ListenException {
        listener.listen(event);
    }
}
