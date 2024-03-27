package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;

public class Corner {
    GameResource resource;
    boolean occupied;
    boolean visible;
    PlaceableCard cardRef;
    CornerDirection direction;

    Corner(GameResource resource,PlaceableCard cardRef, CornerDirection dir){
        this.resource = resource;
        this.cardRef = cardRef;
        this.occupied = false;
        this.visible = true;
        this.direction = dir;
    }
    public PlaceableCard getCardRef() {
        return cardRef;
    }
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
     * @return TRUE if there is no corner covering this corner, FALSE otherwise.
     */
    public boolean isOccupied(){
        return occupied;
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
        if(visible && !occupied) {
            return resource;
        }
        return null;
    }

}
