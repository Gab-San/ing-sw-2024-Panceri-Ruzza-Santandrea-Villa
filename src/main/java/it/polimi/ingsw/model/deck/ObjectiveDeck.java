package it.polimi.ingsw.model.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import it.polimi.ingsw.model.json.deserializers.ObjectiveCardDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The objective deck is composed of 16 objective cards at the start of the game
 * which are then distributed as follows:<br>
 * - 2 cards are revealed on the ground; <br>
 * - 1 card is drawn from the deck by each player. <br>
 */
public class ObjectiveDeck {
    public final List<ObjectiveCard> cardDeck;
    private ObjectiveCard firstRevealed;
    private ObjectiveCard secondRevealed;

    public ObjectiveDeck() throws DeckInstantiationException {
        // Populating the array
        try {
            cardDeck = importFromJson();
        } catch (DeckException deckException){
            throw new DeckInstantiationException(deckException.getMessage(), deckException.getCause(),
                    deckException.getDeck());
        }
        reveal();
    }


    /**
     * This method should be called once at the start of the match by each player
     * @return the first card of the deck
     * @throws DeckException if deck is empty
     */
    public ObjectiveCard getCard() throws DeckException {
        //FIXME [GAMBA] Understand if it's thread safe
        // - There's no need to synchronize since the construction is not threaded
        synchronized (cardDeck) {
            if (cardDeck.isEmpty()) {
                throw new DeckException("The deck is empty", ObjectiveDeck.class);
            }
            return cardDeck.remove(getRandomCard());
        }
    }

    private int getRandomCard() {
        return new Random().nextInt(cardDeck.size());
    }

    /**
     * This method should be called once in the setup
     */
    private void reveal() {
        synchronized (cardDeck) {
            try {
                firstRevealed = getCard();
                secondRevealed = getCard();
            } catch (DeckException emptyDeck) {
                emptyDeck.printStackTrace(System.err);
            }
        }
    }

    public void putCard(ObjectiveCard card){
        synchronized (cardDeck){
            if(!cardDeck.contains(card))
                cardDeck.add(card);
        }
    }

    /**
     * Getter for the first revealed card. This card must be referred to in order to calculate the objective
     * points.
     * @return the first revealed objective card
     */
    public ObjectiveCard getFirstRevealed(){
        return firstRevealed;
    }
    /**
     * Getter for the second revealed card.This card must be referred to in order to calculate the objective
     * points.
     * @return the second revealed objective card
     */
    public ObjectiveCard getSecondRevealed(){
        return secondRevealed;
    }



    private List<ObjectiveCard> importFromJson() throws DeckException {
        try{
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(ObjectiveCard.class, new ObjectiveCardDeserializer());
            mapper.registerModule(module);

            File json = new File("src/main/java/it/polimi/ingsw/model/json/ObjectiveCard.json");
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, ObjectiveCard.class));
        } catch (IOException exc){
            throw new DeckException("Error reading file JSON", exc, ObjectiveDeck.class);
        }

    }
}
