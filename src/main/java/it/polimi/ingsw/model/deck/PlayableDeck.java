package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.listener.GameEvent;
import it.polimi.ingsw.listener.GameListener;
import it.polimi.ingsw.listener.GameSubject;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.deck.cardfactory.CardFactory;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import it.polimi.ingsw.model.exceptions.ListenException;

import java.util.ArrayDeque;
import java.util.Deque;

public class PlayableDeck implements GameSubject {
    private final Deque<PlayCard> cardDeck;
    private final CardFactory cardFactory;
    private PlayCard firstRevealedCard;
    private PlayCard secondRevealedCard;

    private static final int FIRST_POSITION = 1;
    private static final int SECOND_POSITION = 2;


    public PlayableDeck(CardFactory cardFactory, int initialCapacity) throws DeckInstantiationException {
        cardDeck = new ArrayDeque<>(initialCapacity);
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
                    break;
                }
                firstRevealedCard = getTopCard();
                break;
            case SECOND_POSITION:
                if(cardDeck.isEmpty()) {
                    secondRevealedCard = null;
                    break;
                }
                secondRevealedCard = getTopCard();
                break;
            default:
                break;
        }
    }

    public synchronized boolean isEmpty(){
        return cardDeck.isEmpty();
    }
    public synchronized boolean isCompletelyEmpty(){
        return isEmpty() && !hasSecondRevealed() && !hasFirstRevealed();
    }

    public boolean hasFirstRevealed() {
        return firstRevealedCard != null;
    }

    public boolean hasSecondRevealed() {
        return secondRevealedCard != null;
    }

    @Override
    public void addListener(GameListener listener) {

    }

    @Override
    public void removeListener(GameListener listener) {

    }

    @Override
    public void notifyAllListeners(GameEvent event) {

    }

    @Override
    public void notifyListener(GameListener listener, GameEvent event) throws ListenException {

    }
}
