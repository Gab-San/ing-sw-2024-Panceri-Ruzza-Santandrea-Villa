package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The corner class implements the functionality of a corner in the card.
 * The action that can be made on a corner are: <br>
 * - cover it; <br>
 * - count its resource;
 */
public class Corner {
    private final GameResource frontResource;
    private final GameResource backResource;
    private boolean occupied;
    private boolean visible;
    private PlaceableCard cardRef;
    private final CornerDirection direction;
    public Corner(){
        this.frontResource = null;
        this.backResource = null;
        this.occupied = false;
        this.visible = true;
        this.cardRef = null;
        this.direction = null;
    }
    // FIXME:
    //  - changes  made : instead of getting card-ref from corner, using this constr to get card-ref
    //  - this constructor is used only in this package to be sure to copy the information of the corners and
    //  not to have spare refs to objects
    protected Corner(@NotNull Corner otherCorner, @NotNull PlaceableCard cardRef){
        this.frontResource = otherCorner.frontResource;
        this.backResource = otherCorner.backResource;
        this.occupied = otherCorner.occupied;
        this.visible = otherCorner.visible;
        this.cardRef = cardRef;
        this.direction = otherCorner.direction;
    }
    public Corner(@Nullable GameResource frontResource, @Nullable GameResource backResource, PlaceableCard cardRef, CornerDirection dir){
        this.frontResource = frontResource;
        this.backResource = backResource;
        this.cardRef = cardRef;
        this.occupied = false;
        this.visible = true;
        this.direction = dir;
    }
    public Corner(@Nullable GameResource frontResource, @Nullable GameResource backResource, CornerDirection dir){
        this(frontResource, backResource, null, dir);
    }
    public Corner(@Nullable GameResource frontResource, CornerDirection dir){
        this(frontResource, null, dir);
    }

    // TODO: Improve Corner.equals(), currently ignores cardRef
    public boolean equals(Corner other){
        return frontResource == other.frontResource &&
                backResource == other.backResource &&
                direction == other.direction;
                //cardRef.equals(other.cardRef); ?? would be endless recursive loop
    }
    @Override
    public boolean equals(Object other){
        if(other == this) return true;
        if(other instanceof Corner)
            return equals((Corner)other);
        else
            return false;
    }
    /**
     * @return the card that contains this corner, in order to pinpoint the location of it;
     */
    public PlaceableCard getCardRef() {
        return cardRef;
    }
    protected Corner setCardRef(PlaceableCard card){
        cardRef = card;
        return this;
    }

    /**
     * @return the direction of the corner;
     */
    public CornerDirection getDirection() {
        return direction;
    }

    /**
     * This functions sets the occupation of the corner as TRUE.
     * After its invocation the function isOccupied will return TRUE.
     */
    public Corner occupy(){
        occupied = true;
        return this;
    }

    /**
     * @return TRUE if a card can be placed on this corner, FALSE otherwise
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
     * @return  TRUE if the selected corner is not covered by any other corner, FALSE otherwise
     */
    public boolean isVisible(){
        return visible;
    }

    /**
     * @return the resource visible in the corner.
     */
    public GameResource getResource(){
        return visible ? cardRef.isFaceUp() ? frontResource : backResource : null;
    }

}
