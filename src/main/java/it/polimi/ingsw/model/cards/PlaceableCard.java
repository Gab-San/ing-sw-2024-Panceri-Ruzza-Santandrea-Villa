package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;

import java.security.InvalidParameterException;
import java.util.*;

public abstract class PlaceableCard extends Card{
    private final Point position;
    protected final Hashtable<CornerDirection, Corner> corners;

    // init "blank" card as having all empty corners
    protected PlaceableCard(){
        this(
                new Corner(null, null, CornerDirection.TL),
                new Corner(null, null, CornerDirection.TR),
                new Corner(null, null, CornerDirection.BL),
                new Corner(null, null, CornerDirection.BR)
        );
    }

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
        // set any missing corner to FILLED on front side
        for(CornerDirection dir : CornerDirection.values()){
            this.corners.putIfAbsent(dir, new Corner(GameResource.FILLED, null, this, dir));
        }
    }

    /**
     * This constructor builds the card when positioned adding the information of the position.
     * This constructor exists in order to build immutable card objects
     * @param placement coordinates at which it is placed
     * @param oldCard copied card
     */
    protected PlaceableCard(Point placement, PlaceableCard oldCard){
        super(oldCard);
        this.corners = oldCard.corners;
        for (Corner c : corners.values()){
            c.setCardRef(this);
        }
        position = new Point(placement);
    }


    /**
     * @param cornDir indicates the selected corner direction;
     * @return the corner in the selected direction;
     * @throws NoSuchElementException if the corner in the selected direction is filled;
     */
    public Corner getCorner(CornerDirection cornDir){
        return corners.get(cornDir);
    }

    /**
     * @return a List of free corners (not occupied and not filled)
     */
    public List<Corner> getFreeCorners(){
        List<Corner> freeCorners = new LinkedList<>();
        for (CornerDirection dir : CornerDirection.values()){
            Corner c = getCorner(dir);
            if (!c.isOccupied()) freeCorners.add(c);
        }
        return freeCorners;
    }

    /**
     * @return a map with the count of visible resources
     */
    abstract public Map<GameResource, Integer> getCardResources();
    // used in implementations to avoid code duplication
    protected int[] getCornerResources(){
        int[] resourcesCount = new int[7];

        Set<CornerDirection> cornerKeys = corners.keySet();
        for(CornerDirection cornDir: cornerKeys){
            GameResource res = corners.get(cornDir).getResource();
            if(res != null && res != GameResource.FILLED){
                resourcesCount[res.getResourceIndex()]++;
            }
        }
        return resourcesCount;
    }
    // used in implementations to avoid code duplication
    protected Map<GameResource, Integer> resourceArrayToMap(int[] resourcesCount){
        Hashtable<GameResource, Integer> countedResources = new Hashtable<GameResource, Integer>();
        for (GameResource r : GameResource.values()){
            if(r != GameResource.FILLED)
                countedResources.put(r, resourcesCount[r.getResourceIndex()]);
        }

        return countedResources;
    }

    /**
     * @return the resource that identifies the colour of this card
     */
    abstract public GameResource getCardColour();

    public Point getPosition() throws RuntimeException {
        if(position == null){
            throw new RuntimeException("Tried to access position on a card that wasn't placed");
        }
        return position;
    }

    public abstract PlaceableCard setPosition(Point placement);


    // OBJECT METHODS

    /**
     * @param other card to compare to this
     * @return TRUE if cards have the same content (ignoring position), FALSE otherwise
     */
    public boolean equals(PlaceableCard other){
        return super.equals(other) &&
                corners.equals(other.corners);
        // corners.equals delegates comparison to Corner.equals for each corner
    }
    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if(!(other instanceof PlaceableCard)) return false;

        return equals((PlaceableCard) other);
    }
}
