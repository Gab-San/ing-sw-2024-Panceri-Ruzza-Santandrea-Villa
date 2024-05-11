package it.polimi.ingsw.model.cards;

/**
 * This represents a generic card.
 * <p>
 * This is the highest level abstraction of a card, representing the most simple form of it being a blank
 * rectangular that can be flipped.
 * </p>
 * <p>
 * By default when instantiated a card is facing downwards (displaying back)
 * </p>
 */
public abstract class Card {
    private String cardID;
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
        cardID = null;
    }

    /**
     * Constructor of card class that sets the card identifier
     * @param cardID the card identifier
     */
    protected Card(String cardID){
        this();
        this.cardID = cardID;
    }

    /**
     * Constructor of card class that copies the information from another card
     * @param other the card whose info will be copied
     */
    protected Card(Card other){
        isFaceUp = other.isFaceUp;
        cardID = other.cardID;
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

    public String getCardID() {return cardID != "" ? cardID : "";}

    // OBJECT METHODS
    /**
     * This method compares two placeable card objects.
     * <p>
     *     Returns true if the two cards have the same properties:<br>
     *     - Orientation;<br>
     *     Returns false otherwise.
     * </p>
     * @param other the card with which to compare
     * @return true if the card is the same as the argument; false otherwise
     */
    protected boolean compareCard(Card other){
        return isFaceUp == other.isFaceUp;
    }

    @Override
    public String toString() {
        return cardID + "\n" + (isFaceUp ? "Is Facing Up\n" : "Is Facing Down\n");
    }
}
