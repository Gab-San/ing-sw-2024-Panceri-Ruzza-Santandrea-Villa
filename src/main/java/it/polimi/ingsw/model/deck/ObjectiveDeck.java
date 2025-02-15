package it.polimi.ingsw.model.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.json.deserializers.ObjectiveCardDeserializer;
import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.GameSubject;
import it.polimi.ingsw.model.listener.remote.errors.CrashStateError;
import it.polimi.ingsw.model.listener.remote.events.deck.DeckRevealEvent;
import it.polimi.ingsw.model.listener.remote.events.deck.FaceDownReplaceEvent;

import java.io.IOException;
import java.io.InputStream;
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

    private final List<ObjectiveCard> cardDeck;
    private ObjectiveCard firstRevealed;
    private ObjectiveCard secondRevealed;
    private ObjectiveCard topCard;
    private final List<GameListener> gameListenerList;
    /**
     * Default constructor. Creates the objective deck and sets the top card.
     */
    public ObjectiveDeck() throws IllegalStateException {
        gameListenerList = new LinkedList<>();
        // Populating the array
        cardDeck = importFromJson();

        topCard = cardDeck.remove(getRandomCard());
        firstRevealed = null;
        secondRevealed = null;
    }


    /**
     * This method should be called once at the start of the match by each player
     * @return the first card of the deck
     */
    public synchronized ObjectiveCard getCard(){
        ObjectiveCard returnCard = topCard;
        if(cardDeck.isEmpty()){
            topCard = null;
        } else {
            topCard = cardDeck.remove(getRandomCard());
        }

        notifyAllListeners(new FaceDownReplaceEvent(Board.OBJECTIVE_DECK, topCard));
        return returnCard;
    }

    /**
     * Returns the top card of the face-down pile of the deck.
     * @return face-down pile top card
     */
    public ObjectiveCard peekTop(){
        return topCard;
    }

    private int getRandomCard() {
        return new Random().nextInt(cardDeck.size());
    }

    /**
     * This method should be called once per game during setup.
     * If called more than once it doesn't change the status of the deck.
     */
    public synchronized void reveal() {
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
    public synchronized ObjectiveCard getFirstRevealed(){
        return firstRevealed;
    }
    /**
     * Getter for the second revealed card.This card must be referred to in order to calculate the objective
     * points.
     * @return the second revealed objective card
     */
    public synchronized ObjectiveCard getSecondRevealed(){
        return secondRevealed;
    }

    private InputStream getJsonFromResources(){
        ClassLoader cl = this.getClass().getClassLoader();
        try {
            return cl.getResourceAsStream("server/ObjectiveCard.json");
        } catch(NullPointerException e){
            System.exit(-1); //crash app if a json can't be read
            throw new RuntimeException("File could not be read");
        }
    }

    private List<ObjectiveCard> importFromJson() throws IllegalStateException{
        List<ObjectiveCard> objectiveCardList;

        try{
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(ObjectiveCard.class, new ObjectiveCardDeserializer());
            mapper.registerModule(module);

            InputStream json = getJsonFromResources();
            objectiveCardList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, ObjectiveCard.class));
        } catch (IOException exc){
            notifyAllListeners(new CrashStateError("all", "An error occured while initializing the objective deck".toUpperCase()));
            System.exit(-1);
            throw new IllegalStateException("SYSTEM SHUT DOWN: An error occured while initializing the objective deck");
        }

        return objectiveCardList;
    }

    /**
     * Returns true if the deck is empty, false otherwise.
     * @return ture if the deck is empty, false otherwise
     */
    public boolean isEmpty(){
        return cardDeck.isEmpty();
    }

    @Override
    public void addListener(GameListener listener) {
        synchronized (gameListenerList) {
            gameListenerList.add(listener);
        }
    }

    @Override
    public void removeListener(GameListener listener) {
        synchronized (gameListenerList) {
            gameListenerList.remove(listener);
        }
    }

    @Override
    public void notifyAllListeners(GameEvent event) throws ListenException {
        synchronized (gameListenerList) {
            for (GameListener listener : gameListenerList) {
                listener.listen(event);
            }
        }
    }
}
