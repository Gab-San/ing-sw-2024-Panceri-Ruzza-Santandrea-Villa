package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import org.jetbrains.annotations.*;

/**
 * The corner class implements the functionality of a corner in the card.
 * The action that can be made on a corner are: <br>
 * - cover it; <br>
 * - count its resource; <br>
 * - access the related card.
 */
public class Corner {
    private final GameResource frontResource;
    private final GameResource backResource;
    private boolean occupied;
    private boolean visible;
    private PlaceableCard cardRef;
    private final CornerDirection direction;

    /**
     * This constructor is used during instantiation of cards to make sure of not compromising
     * the information contained into the corner.
     * @param otherCorner the corner from which to copy information
     * @param cardRef the card with which the corner is associated
     */
    Corner(@NotNull Corner otherCorner, @NotNull PlaceableCard cardRef){
        this.frontResource = otherCorner.frontResource;
        this.backResource = otherCorner.backResource;
        this.occupied = otherCorner.occupied;
        this.visible = otherCorner.visible;
        this.cardRef = cardRef;
        this.direction = otherCorner.direction;
    }
    /**
     * This constructor builds a corner given all the information about it.
     * @param frontResource the resource displayed on the front face of the card
     * @param backResource the resource displayed on the back face of the card
     * @param cardRef the card with which the corner is associated
     * @param dir the direction of the corner
     */
    public Corner(@Nullable GameResource frontResource, @Nullable GameResource backResource, PlaceableCard cardRef, CornerDirection dir){
        this.frontResource = frontResource;
        this.backResource = backResource;
        this.occupied = false;
        this.visible = true;
        this.cardRef = cardRef;
        this.direction = dir;
    }

    /**
     * This constructor builds a corner with a null cardRef
     * @param frontResource the resource displayed on the front face of the card
     * @param backResource the resource displayed on the back face of the card
     * @param dir the direction of the corner
     */
    public Corner(@Nullable GameResource frontResource, @Nullable GameResource backResource, CornerDirection dir){
        this(frontResource, backResource, null, dir);
    }

    /**
     * This constructor defaults the back resource to an empty resource.
     * @param frontResource the resource displayed on the front face of the card
     * @param dir the direction of the corner
     */
    public Corner(@Nullable GameResource frontResource, CornerDirection dir){
        this(frontResource, null, dir);
    }


    /**
     * Getter for the reference of the card related to this corner.
     * This method is used to access the information of the card given the corner.
     * @return the card that contains this corner
     * @throws RuntimeException if it is a "detached" corner, as in not connected to a card
     */
    public PlaceableCard getCardRef() throws IllegalStateException {
        if(cardRef == null) throw new IllegalStateException("Error trying to access card of a 'detached' Corner.");
        return cardRef;
    }

    /**
     * This method is used to change the card reference when the card is placed.
     * Placing a card will result in making a copy of the original into the matrix so a change
     * in the reference is needed in order to correctly access information from the corners
     * @param card the new card to which the corner must be associated with
     */
    Corner setCardRef(PlaceableCard card){
        cardRef = card;
        return this;
    }

    /**
     * Getter for the direction of the corner.
     * @return the direction of the corner;
     */
    public CornerDirection getDirection() {
        return direction;
    }

    /**
     * This functions sets the occupation of the corner as true.
     * After its invocation the function isOccupied will return true.
     */
    public Corner occupy(){
        occupied = true;
        return this;
    }

    /**
     * Getter for the boolean value of occupation of the corner.
     * It gives information about the availability of the corner on which to place a card.
     * @return true if a card can be placed on this corner, false otherwise
     */
    public boolean isOccupied(){
        return occupied || getResource() == GameResource.FILLED;
    }

    /**
     * This method sets the visibility of the corner.
     * After its invocation the method isVisible will return FALSE.
     */
    public Corner cover(){
        visible = false;
        return this;
    }

    /**
     * Getter for the boolean value of visibility of the corner.
     * It returns whether the corner is covered by another card's corner or not.
     * @return  TRUE if the selected corner is not covered by any other corner, FALSE otherwise
     */
    public boolean isVisible(){
        return visible;
    }

    /**
     * Getter for the corner resource.
     * An empty corner will return an empty resource, a covered corner will not return a resource.
     * @return the resource visible in the corner, depending on the orientation of the card.
     */
    public GameResource getResource(){
        if(!visible){
            return null;
        }

        return cardRef.isFaceUp ? frontResource : backResource;
    }

    // OBJECT METHODS
    /**
     * Indicates whether some object has the same properties as this one
     * @param other the reference object which to compare
     * @return true if the object is the same as the argument; false otherwise
     */
    @Override
    public boolean equals(Object other){
        if(other == this) return true;
        if(!(other instanceof Corner)) return false;

        return compare((Corner)other);
    }

    /**
     * This method compare two corners and defines whether they are equal.
     * <p>
     *     The parameters checked are:<br>
     *     - Front resource;<br>
     *     - Back resource;<br>
     *     - Direction;<br>
     *     - Occupation and visibility;<br>
     * </p>
     * @param other the corner with which to compare
     * @return true if the two corners are equal; false otherwise
     */
    public boolean compare(Corner other){
        return frontResource == other.frontResource &&
                backResource == other.backResource &&
                (cardRef == null || other.cardRef == null || cardRef.compareCard(other.cardRef))&&
                direction == other.direction &&
                occupied == other.occupied &&
                visible == other.visible;
    }


    @Override
    public String toString() {
        return  "[occupied]" + occupied +
                "[visible]" + visible +
                "[frontRes]" + frontResource +
                "[backRes]" + backResource + "\n";
    }
}
