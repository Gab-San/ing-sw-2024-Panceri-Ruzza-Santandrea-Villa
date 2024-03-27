package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.GameResource;

public class Corner {
    GameResource resource;
    boolean occupied;
    boolean visible;
    PlaceableCard cardRef;
    Corner(GameResource resource,PlaceableCard cardRef){
        this.resource = resource;
        this.cardRef = cardRef;
        this.occupied = false;
        this.visible = true;
    }

    /**
     * This functions sets the occupation of the corner as TRUE.
     * After its invocation the function isOccupied will return TRUE.
     */
    public void occupy(){
        occupied = true;
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
    public void cover(){
        visible = false;
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
