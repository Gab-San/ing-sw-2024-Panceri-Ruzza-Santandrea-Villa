package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.deck.cardfactory.CardFactory;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PlayableDeck {
    private final BlockingQueue<PlayCard> cardDeck;
    private final CardFactory cardFactory;
    private PlayCard firstRevealedCard;
    private PlayCard secondRevealedCard;

    private static final int FIRST_POSITION = 1;
    private static final int SECOND_POSITION = 2;

//    private boolean emptied;

    public PlayableDeck(CardFactory cardFactory) throws DeckInstantiationException {
        cardDeck = new ArrayBlockingQueue<>(6,true);
        this.cardFactory = cardFactory;
        try {
            fillDeck(1);
        } catch (DeckException deckException){
            throw new DeckInstantiationException("Error while filling the deck", deckException, PlayableDeck.class);
        }
        reveal(FIRST_POSITION);
        reveal(SECOND_POSITION);
    }

    private void fillDeck(int remainingCapacity) throws DeckException {
        while (cardDeck.remainingCapacity() > remainingCapacity){
            try {
                cardDeck.put(cardFactory.addCardToDeck());
            } catch (InterruptedException e) {
                //TODO handle exception
            }
        }
    }

    public synchronized PlayCard getTopCard() throws DeckException{
        if(cardDeck.isEmpty()){
            throw new DeckException("Deck is empty", PlayableDeck.class);
        }

        PlayCard returnCard;

        try {
            returnCard = cardDeck.take();
        } catch (InterruptedException exception) {
            throw new DeckException(exception, PlayableDeck.class);
        }

        new Thread(() -> {
            try {
                fillDeck(3);
            } catch (DeckException ignored) {
            }
        }).start();
        return returnCard;
    }

    public synchronized PlayCard getFirstRevealedCard() throws DeckException {
        if(firstRevealedCard == null){
            throw new DeckException("Trying to draw from empty position", PlayableDeck.class);
        }

        PlayCard returnCard = firstRevealedCard;
        reveal(FIRST_POSITION);
        return returnCard;
    }

    public synchronized PlayCard getSecondRevealedCard() throws DeckException{
        if(secondRevealedCard == null){
            throw new DeckException("Trying to draw from empty position", PlayableDeck.class);
        }

        PlayCard returnCard = secondRevealedCard;
        reveal(SECOND_POSITION);
        return returnCard;
    }


    private void reveal(int position) throws IllegalStateException{
        switch(position){
            case FIRST_POSITION:
                try {
                    firstRevealedCard = getTopCard();
                } catch (DeckException deckException){
                    firstRevealedCard = null;
                }
                break;
            case SECOND_POSITION:
                try {
                    secondRevealedCard = getTopCard();
                } catch (DeckException deckException){
                    secondRevealedCard = null;
                }
                break;
            default:
                throw new IllegalStateException("Trying to position in non-position");
        }
    }

}
