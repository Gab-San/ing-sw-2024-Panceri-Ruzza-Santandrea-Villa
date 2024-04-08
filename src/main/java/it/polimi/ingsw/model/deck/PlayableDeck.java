package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.deck.cardfactory.CardFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PlayableDeck {
    private final BlockingQueue<PlayCard> cardDeck;
    private PlayCard firstRevealedCard;
    private PlayCard secondRevealedCard;
    public PlayableDeck(CardFactory cardFactory){
        cardDeck = new ArrayBlockingQueue<>(6,true);
        CardFiller fillThread = new CardFiller(cardFactory);
        fillThread.setDaemon(true);
        fillThread.start();
        //FIXME this is setup related. Should we do this in the setup and make it possible to access those cards
        // to set them up correctly?
        firstRevealedCard = reveal();
        secondRevealedCard = reveal();
    }

    public PlayCard getTopCard() throws Exception{
        if(cardDeck.isEmpty()){
            throw new Exception("This deck is empty");
        }

        return cardDeck.take();
    }

    public PlayCard getFirstRevealedCard() throws Exception{
        if (firstRevealedCard == null) {
            throw new Exception("This deck is empty");
        }
        PlayCard toReturn = firstRevealedCard;
        firstRevealedCard = reveal();
        return toReturn;
    }

    public PlayCard getSecondRevealedCard() throws Exception{
        if(secondRevealedCard == null){
            throw new Exception("This deck is empty");
        }
        PlayCard toReturn = secondRevealedCard;
        secondRevealedCard = reveal();
        return toReturn;
    }

    private PlayCard reveal(){
        //TODO implement correctly this method
        return null;
    }

    private class CardFiller extends Thread {
        CardFactory cardFactory;
        CardFiller(CardFactory cardFactory){
            this.cardFactory = cardFactory;
        }

        @Override
        public void run() {
            while (true){
                try {
                    PlayCard newPlayCard = cardFactory.addCardToDeck();
                    cardDeck.put(newPlayCard);
                } catch (InterruptedException interruptedException){
                    //TODO handle exception
                } catch (Exception emptyDeck){
                    //TODO handle exception
                    return;
                }
            }
        }
    }
}
