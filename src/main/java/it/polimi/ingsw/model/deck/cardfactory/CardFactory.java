package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.exceptions.DeckException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class CardFactory{
    protected final List<String> remainingCards;

    protected CardFactory(String idFile){
        Charset chrset = Charset.forName(System.getProperty("file.encoding"));
        remainingCards = loadCardsIDs(Path.of(idFile), chrset);
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

    private List<String> loadCardsIDs(Path filePath, Charset charset){
        List<String> idList = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(filePath, charset)){
            reader.lines().flatMap(e -> Arrays.stream(e.trim().split("\\s+"))).forEach(idList::add);
        } catch(IOException ioException){
            // TODO: [GAMBA] Handle exception
            ioException.printStackTrace(System.err);
        }
        return idList;
    }

    public synchronized boolean isEmpty(){
        return remainingCards.isEmpty();
    }
}
