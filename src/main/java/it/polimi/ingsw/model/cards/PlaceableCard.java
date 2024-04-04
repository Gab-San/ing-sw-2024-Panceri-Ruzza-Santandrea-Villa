package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;

import java.util.*;

public abstract class PlaceableCard extends Card{
    private Point position;
    protected Hashtable<CornerDirection, Corner> corners;

    /**
     * @param cornDir indicates the selected corner direction;
     * @return the corner in the selected direction;
     * @throws NoSuchElementException if the corner in the selected direction is filled;
     */
    public Corner getCorner(CornerDirection cornDir) throws NoSuchElementException{
        if(!corners.containsKey(cornDir)){
            throw new NoSuchElementException("The searched corner is filled");
        }
        return corners.get(cornDir);
    }

    /**
     * @return a List of free corners (not occupied and not filled)
     */
    public List<Corner> getFreeCorners(){
        List<Corner> freeCorners = new LinkedList<>();
        for (CornerDirection dir : CornerDirection.values()){
            try{
                Corner c = getCorner(dir);
                if (!c.isOccupied()) freeCorners.add(c);
            }catch (Exception ignored){} // filled corner isn't free
        }
        return freeCorners;
    }

    /**
     * @return a map with the count of resources to add to the visible resources count on the play area
     */
    abstract public Map<GameResource, Integer> getCardResources();
    abstract public GameResource getCardColor();

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
