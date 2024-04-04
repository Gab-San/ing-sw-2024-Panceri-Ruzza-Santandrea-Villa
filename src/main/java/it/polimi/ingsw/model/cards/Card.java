package it.polimi.ingsw.model.cards;

/**
 * This abstraction is used to then render all the cards using the same abstract class
 */
public abstract class Card {
    /**
     * Describes the orientation of the card. If FALSE it is on the front side (it has not been flipped);
     * if TRUE is on the back side.
     */
    protected boolean flipped;

    /**
     * @return TRUE if the card is on the back side, FALSE if it's on the front side.
     */
    public boolean isFlipped(){
        return flipped;
    }
}
