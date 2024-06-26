package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.exceptions.DeckException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This class represents an abstraction of a card factory, an object that instantiates
 * card objects as requested.
 */
public abstract class CardFactory{
    /**
     * List of cards still remaining in the card factory, representing the full
     * deck of cards possibly used.
     */
    protected final List<String> remainingCards;

    /**
     * Constructs a card factory for the deck coded within the file path.
     * @param idFile path to the deck file
     */
    protected CardFactory(String idFile){
        Charset charset = Charset.forName(Charset.defaultCharset().displayName());
        remainingCards = loadCardsIDs(getFromResources(idFile), charset);
    }

    /**
     * Returns a random card to add to the deck.
     * @return a random card in the deck
     * @throws DeckException if the deck is empty
     */
    abstract public PlayCard addCardToDeck() throws DeckException;

    /**
     * Instantiates the requested card such as Card.equals(o, e) is true.
     * @param cardId card identifier
     * @return requested card
     */
    abstract protected PlayCard instantiateCard(String cardId);

    /**
     * Returns the index of a random card within the remaining cards.
     * @return remaining card random index
     */
    protected int getRandomCard() {
        return new Random().nextInt(remainingCards.size());
    }

    private List<String> loadCardsIDs(InputStream idStream, Charset charset){
        List<String> idList = new ArrayList<>();
        InputStreamReader idStreamReader = new InputStreamReader(idStream, charset);
        try(BufferedReader reader = new BufferedReader(idStreamReader)){
            reader.lines().flatMap(e -> Arrays.stream(e.trim().split("\\s+"))).forEach(idList::add);
        } catch(IOException ioException){
            System.exit(-1);
        }
        return idList;
    }

    /**
     * Searches and loads the file with the specified file name.
     * @param fileName name of the file
     * @return file input stream
     */
    protected InputStream getFromResources(String fileName){
        ClassLoader cl = this.getClass().getClassLoader();
        try {
            return cl.getResourceAsStream("server/" + fileName);
        } catch(NullPointerException e){
            System.exit(-1); //crash app if a json can't be read
            throw new RuntimeException("File could not be read");
        }
    }

    /**
     * Returns true if there are no remaining cards, false otherwise.
     * @return true if there are no remaining cards, false otherwise
     */
    public synchronized boolean isEmpty(){
        return remainingCards.isEmpty();
    }
}
