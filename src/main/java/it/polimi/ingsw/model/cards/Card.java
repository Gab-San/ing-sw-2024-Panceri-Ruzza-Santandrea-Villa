package it.polimi.ingsw.model.cards;

/**
 * This represents a generic card.
 * <p>
 * This is the highest level abstraction of a card, representing the most simple form of it being a blank
 * rectangular that can be flipped.
 */
public abstract class Card {
    /**
     * Describes the orientation of the card. If FALSE it is on the back side;
     * if TRUE is on the front side.
     */
    protected boolean isFaceUp;

    /**
     * Default constructor of the card class.
     * Sets the face of the card down.
     */
    protected Card(){
        isFaceUp = false;
    }

    /**
     * Constructor of card class that copies the information from another card
     * @param other the card whose info will be copied
     */

    protected Card(Card other){
        isFaceUp = other.isFaceUp;
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
     * Returns true if the card is on the front side, false if it's on the back side.
     * @return true if the card is facing up; false otherwise.
     */
    public synchronized boolean isFaceUp(){
        return isFaceUp;
    }

    // OBJECT METHODS

    /**
     * Indicates whether some object has the same properties as this one
     * @param other the reference object which to compare
     * @return true if the object is the same as the argument; false otherwise
     */
    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if(!(other instanceof Card)) return false;

        return compare((Card) other);
    }

    /**
     * This method compares two placeable card objects.
     * <p>
     *     Returns true if the two cards have the same properties:
     *     - Orientation;
     *     Returns false otherwise.
     * </p>
     * @param other the card with which to compare
     * @return true if the card is the same as the argument; false otherwise
     */
    protected boolean compare(Card other){
        return isFaceUp == other.isFaceUp;
    }
}
