package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.deck.cardfactory.CardFactory;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;

import java.util.ArrayDeque;
import java.util.Deque;

public class PlayableDeck {
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

    public synchronized PlayCard getTopCard() throws DeckException{
        if(cardDeck.isEmpty()){
            throw new DeckException("Deck is empty", PlayableDeck.class);
        }

        PlayCard returnCard;
        returnCard = cardDeck.remove();
        try {
            cardDeck.add(cardFactory.addCardToDeck());
        } catch (DeckException ignored){
            // If the cardFactory is empty there's no mean to throw an error
        }
        return returnCard;
    }

    //[FLAVIO] it's needed for testing, I need to know if firstRevealedCard exists, with a throw it's impossible
    public boolean existFirstRevealedCard(){return !(firstRevealedCard==null);}
    //[FLAVIO] it's needed for testing, I need to know if secondRevealedCard exists, with a throw it's impossible
    public boolean existSecondRevealedCard(){return !(secondRevealedCard==null);}

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

    public synchronized boolean isTopEmpty(){
        return cardFactory.isEmpty() && cardDeck.isEmpty();
    }
    public synchronized boolean isCompletelyEmpty(){
        return isTopEmpty() && firstRevealedCard == null && secondRevealedCard == null;
    }

}
