package it.polimi.ingsw.model.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.json.deserializers.StartingCardDeserializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents the deck of starting cards.
 */
public class StartingCardDeck{

    private final List<StartingCard> cardDeck;

    /**
     * Default constructor. Creates the starting deck and imports all starting cards.
     */
    public StartingCardDeck() throws IllegalStateException{
        cardDeck = importFromJson();
    }

    /**
     * Returns a random starting card from the remaining cards of the deck.
     * @return a random starting card from the remaining cards of the deck.
     */
    public StartingCard getCard(){
        int cardIdx = getRandomCard();
        return cardDeck.remove(cardIdx);
    }

    /**
     * Creates an {@link InputStream} that reads from the starting card Json.
     * @return an {@link InputStream} that reads from the starting card Json.
     */
    private InputStream getJsonFromResources(){
        ClassLoader cl = this.getClass().getClassLoader();
        try {
            return cl.getResourceAsStream("server/StartingCard.json");
        } catch(NullPointerException e){
            System.exit(-1); //crash app if a json can't be read
            throw new RuntimeException("File could not be read");
        }
    }

    /**
     * Imports and instantiates all starting cards described in the startingCard json.
     * @return a list of all imported starting cards.
     * @throws IllegalStateException if an I/O error occurs.
     */
    private List<StartingCard> importFromJson() throws IllegalStateException{
        List<StartingCard> startingCardsList;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(StartingCard.class, new StartingCardDeserializer());
            objectMapper.registerModule(simpleModule);

            InputStream json = getJsonFromResources();
            startingCardsList = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, StartingCard.class));
        } catch (IOException e) {
            throw new IllegalStateException("SYSTEM SHUT DOWN: An error occured while initializing the starting deck");
        }

        return startingCardsList;
    }

    /**
     * Returns a random index pointing to a card in this deck.
     * @return a random index pointing to a card in this deck.
     */
    protected int getRandomCard() {
        return new Random().nextInt(cardDeck.size());
    }
}
