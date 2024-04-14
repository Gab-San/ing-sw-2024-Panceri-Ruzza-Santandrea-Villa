package it.polimi.ingsw.model.cards;

/**
 * This represents a generic card.
 *
 * This is the higher level abstraction of a card, representing the most simple form of it being a blank
 * rectangular that can be flipped.
 */
public abstract class Card {
    /**
     * Describes the orientation of the card. If FALSE it is on the back side;
     * if TRUE is on the front side.
     */
    protected boolean isFaceUp;

    protected Card(){
        isFaceUp = false;
    }
    protected Card(Card other){
        isFaceUp = other.isFaceUp;
    }
    public boolean equals(Card other){
        return isFaceUp == other.isFaceUp;
    }
    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if(!(other instanceof Card)) return false;
        else return equals((Card) other);
    }

    /**
     * Flips the card.
     */
    public synchronized void flip(){
        isFaceUp = !isFaceUp;
        // eventual GUI code here
    }

    /**
     * Sets the face of the card up.
     */
    public synchronized void turnFaceUp(){
        if(!isFaceUp) flip();
    }

    /**
     * Sets the face of the card down.
     */
    public synchronized void turnFaceDown(){
        if(isFaceUp) flip();
    }
    /**
     * Checks if the card is facing up.
     * @return TRUE if the card is on the front side, FALSE if it's on the back side.
     */
    public synchronized boolean isFaceUp(){
        return isFaceUp;
    }
}
