package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.cards.PlayCard;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public abstract class CardFactory extends Thread{
    protected List<String> remainingCards;

    CardFactory(String idFile){
        Charset chrset = Charset.forName(System.getProperty("file.encoding"));
        remainingCards = CardReader.loadCardsIDs(Path.of(idFile), chrset);
    }

    /**
     * Returns a random card to add to the deck.
     * @return a random card in the deck
     * @throws Exception if the deck is empty
     */
    abstract public PlayCard addCardToDeck() throws Exception;
    abstract protected PlayCard instantiateCard();
    abstract protected void importFromJSON();

    protected int getRandomCardID(){
        return new Random().nextInt(remainingCards.size());
    }

}
