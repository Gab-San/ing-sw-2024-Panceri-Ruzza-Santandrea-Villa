package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;

import java.security.InvalidParameterException;
import java.util.*;

public abstract class PlaceableCard extends Card{
    private final Point position;
    protected final Hashtable<CornerDirection, Corner> corners;

    /**
     * This constructor builds the card, without considering the fact that it might have a position
     * @param corners a list of the corners that the card contains
     */
    protected PlaceableCard(Corner... corners) throws InvalidParameterException{
        this.position = null;
        this.corners = new Hashtable<>();
        for(Corner corn: corners){
            Corner newCorner = new Corner(corn, this);
            if(this.corners.get(newCorner.getDirection()) != null)
                throw new InvalidParameterException("Duplicate corner found in card instantiation");
            this.corners.put(newCorner.getDirection(), newCorner);
        }
    }

    /**
     * This constructor builds the card when positioned adding the information of the position.
     * This constructor exists in order to build immutable card objects
     * @param placement coordinates at which it is placed
     * @param oldCard copied card
     */
    protected PlaceableCard(Point placement, PlaceableCard oldCard){
        this.corners = oldCard.corners;
        position = new Point(placement);
    }


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
    //FIXME:
    //  - current getCorner() implementation does not check for the card being flipped
    //  - on card flipped, all corners should exist

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
     * @return a map with the count of visible resources
     */
    abstract public Map<GameResource, Integer> getCardResources();
    //FIXME:
    // - should check if corner is visible?
    // - should check if already checked?

    /**
     * @return the resource that identifies the colour of this card
     */
    abstract public GameResource getCardColour();

    public Point getPosition() throws RuntimeException {
        if(position == null){
            throw new RuntimeException();
        }
        return position;
    }

    public abstract PlaceableCard setPosition(Point placement);
}
