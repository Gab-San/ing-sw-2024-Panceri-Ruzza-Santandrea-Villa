package it.polimi.ingsw.model.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.GameSubject;
import it.polimi.ingsw.model.listener.remote.events.deck.DeckRevealEvent;
import it.polimi.ingsw.model.listener.remote.events.deck.DeckStateUpdateEvent;
import it.polimi.ingsw.model.listener.remote.events.deck.DrawnCardEvent;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.json.deserializers.ObjectiveCardDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * The objective deck is composed of 16 objective cards at the start of the game
 * which are then distributed as follows:<br>
 * - 2 cards are revealed on the ground; <br>
 * - 1 card is drawn from the deck by each player. <br>
 */
public class ObjectiveDeck implements GameSubject {
    public final List<ObjectiveCard> cardDeck;
    private ObjectiveCard firstRevealed;
    private ObjectiveCard secondRevealed;
    private ObjectiveCard topCard;
    private final List<GameListener> gameListenersList;

    public ObjectiveDeck() throws DeckInstantiationException {
        gameListenersList = new LinkedList<>();
        // Populating the array
        try {
            cardDeck = importFromJson();
        } catch (DeckException deckException){
            throw new DeckInstantiationException(deckException.getMessage(), deckException.getCause(),
                    deckException.getDeck());
        }

        topCard = cardDeck.remove(getRandomCard());
        firstRevealed = null;
        secondRevealed = null;
    }


    /**
     * This method should be called once at the start of the match by each player
     * @return the first card of the deck
     */
    public ObjectiveCard getCard(){
        ObjectiveCard returnCard = topCard;
        if(cardDeck.isEmpty()){
            topCard = null;
        } else {
            topCard = cardDeck.remove(getRandomCard());
        }

        notifyAllListeners(new DrawnCardEvent(Board.OBJECTIVE_DECK, topCard));
        return returnCard;
    }

    private int getRandomCard() {
        return new Random().nextInt(cardDeck.size());
    }

    /**
     * This method should be called once per game during setup.
     * If called more than once it doesn't change the status of the deck.
     */
    public void reveal() {
        if(firstRevealed != null) return;
        firstRevealed = getCard();
        notifyAllListeners(new DeckRevealEvent(Board.OBJECTIVE_DECK, firstRevealed, PlayableDeck.FIRST_POSITION));
        secondRevealed = getCard();
        notifyAllListeners(new DeckRevealEvent(Board.OBJECTIVE_DECK, secondRevealed, PlayableDeck.SECOND_POSITION));
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

    public boolean isEmpty(){
        return cardDeck.isEmpty();
    }

    @Override
    public void addListener(GameListener listener) {
        gameListenersList.add(listener);
        notifyListener(listener, new DeckStateUpdateEvent(Board.OBJECTIVE_DECK, topCard, firstRevealed, secondRevealed));
    }

    @Override
    public void removeListener(GameListener listener) {
        gameListenersList.remove(listener);
    }

    @Override
    public void notifyAllListeners(GameEvent event) {
        for(GameListener listener: gameListenersList){
            listener.listen(event);
        }
    }

    @Override
    public void notifyListener(GameListener listener, GameEvent event) throws ListenException {
        listener.listen(event);
    }
}
