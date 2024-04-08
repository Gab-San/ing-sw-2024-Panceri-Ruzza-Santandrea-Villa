package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.cards.ObjectiveCard;

/**
 * The objective deck is composed of 16 objective cards at the start of the game
 * which are then distributed as follows:<br>
 * - 2 cards are revealed on the ground; <br>
 * - 1 card is drawn from the deck by each player. <br>
 */
public class ObjectiveDeck {
    private final ObjectiveCard[] remainingCards;
    private int lastCard;
    private ObjectiveCard firstRevealed;
    private ObjectiveCard secondRevealed;

    // It's impossible to empty this deck
    // TODO decide if to throw an exception

    public ObjectiveDeck(){
        remainingCards = new ObjectiveCard[16];
        lastCard = remainingCards.length - 1;
        reveal();
    }

    /**
     * This method should be called once at the start of the match by each player
     * @return the first card of the deck
     * @throws Exception if deck is empty
     */
    public ObjectiveCard getCard() throws Exception{
        synchronized (remainingCards) {
            if (lastCard < 0) {
                throw new Exception("The deck is empty");
            }
            ObjectiveCard toReturn = remainingCards[lastCard];
            lastCard--;
            return toReturn;
        }
    }

    /**
     * This method should be called once in the setup
     */
    private void reveal() {
        synchronized (remainingCards) {
            try {
                firstRevealed = getCard();
                secondRevealed = getCard();
            } catch (Exception emptyDeck) {
                emptyDeck.printStackTrace();
            }
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
}
