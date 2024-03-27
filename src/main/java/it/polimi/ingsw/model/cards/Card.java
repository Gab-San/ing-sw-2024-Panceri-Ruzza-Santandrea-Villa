package it.polimi.ingsw.model.cards;

/**
 * This abstraction is used to then render all the cards using the same abstract class
 */
public abstract class Card {
    //TODO decide if flipped should be friendly/protected or private with isFlipped()
    protected boolean flipped;

    /**
     * @return TRUE if the card is on the back side, FALSE if it's on the front side
     * @author Gamba
     */
    public boolean isFlipped(){
        return flipped;
    }
}
