package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.exceptions.DeckException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class CardFactory{
    protected final List<String> remainingCards;

    protected CardFactory(String idFile){
        Charset charset = Charset.forName(System.getProperty("file.encoding"));
        remainingCards = loadCardsIDs(getFromResources(idFile), charset);
    }

    /**
     * Returns a random card to add to the deck.
     * @return a random card in the deck
     * @throws DeckException if the deck is empty
     */
    abstract public PlayCard addCardToDeck() throws DeckException;
    abstract protected PlayCard instantiateCard(String cardId);
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

    protected InputStream getFromResources(String fileName){
        ClassLoader cl = this.getClass().getClassLoader();
        try {
            return cl.getResourceAsStream("server/" + fileName);
        } catch(NullPointerException e){
            System.exit(-1); //crash app if a json can't be read
            throw new RuntimeException("File could not be read");
        }
    }

    public synchronized boolean isEmpty(){
        return remainingCards.isEmpty();
    }
}
