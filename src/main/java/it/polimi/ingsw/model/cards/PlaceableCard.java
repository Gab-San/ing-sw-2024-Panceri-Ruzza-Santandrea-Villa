package it.polimi.ingsw.model.cards;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class represents a placeable card.
 * <p>
 *     In the context of this game a placeable card is a card that can be placed on the map.
 *     It is still a primitive representation of the whole card object, but it already is enough
 *     to define placement rules.
 * </p>
 * <p>
 *     By default when instantiated a card is facing downwards (displaying back)
 * </p>
 */
public abstract class PlaceableCard extends Card{
    private final GamePoint position;
    /**
     * The corners the card is composed of.
     */
    protected final Hashtable<CornerDirection, Corner> corners;

    /**
     * Default constructor: builds a "blank" card with all corners empty on both sides.
    */
    protected PlaceableCard(){
        this(
                new Corner(null, null, CornerDirection.TL),
                new Corner(null, null, CornerDirection.TR),
                new Corner(null, null, CornerDirection.BL),
                new Corner(null, null, CornerDirection.BR)
        );
    }

    /**
     * This constructor builds a card given its corners.
     * <p>
     *     The card position is set to null.
     * </p>
     * @param corners a list of the corners that the card contains
     * @throws InvalidParameterException when a duplicate corner is found
     */
    protected PlaceableCard(String cardID, Corner... corners) throws InvalidParameterException{
        super(cardID);
        this.position = null;

        // For each defined corner a copy is made into the card so that no outside reference can
        // modify the corner once the card is instantiated. To access a corner one must access the card.
        this.corners = new Hashtable<>();
        for(Corner corn: corners){
            Corner newCorner = new Corner(corn, this);
            if(this.corners.get(newCorner.getDirection()) != null)
                throw new InvalidParameterException("Duplicate corner found in card instantiation");
            this.corners.put(newCorner.getDirection(), newCorner);
        }

        // Set any missing corner to FILLED on front side
        for(CornerDirection dir : CornerDirection.values()){
            this.corners.putIfAbsent(dir, new Corner(GameResource.FILLED, null, this, dir));
        }
    }

    /**
     * Constructs an anonymous card given its corners.
     * @param corners corners that the card contains
     * @throws InvalidParameterException when a duplicate corner is found
     */
    protected PlaceableCard(Corner... corners) throws InvalidParameterException{
        this(null, corners);
    }

    /**
     * Constructs a card of the given id with the specified list corners.
     * @param cardID card unique identifier
     * @param corners list of corners contained in the card
     * @throws InvalidParameterException when a duplicate corner is found
     */
    protected PlaceableCard(String cardID, List<Corner> corners) throws InvalidParameterException{
        super(cardID);
        this.position = null;

        // For each defined corner a copy is made into the card so that no outside reference can
        // modify the corner once the card is instantiated. To access a corner one must access the card.
        this.corners = new Hashtable<>();
        for(Corner corn: corners){
            Corner newCorner = new Corner(corn, this);
            if(this.corners.get(newCorner.getDirection()) != null)
                throw new InvalidParameterException("Duplicate corner found in card instantiation");
            this.corners.put(newCorner.getDirection(), newCorner);
        }

        // Set any missing corner to FILLED on front side
        for(CornerDirection dir : CornerDirection.values()){
            this.corners.putIfAbsent(dir, new Corner(GameResource.FILLED, null, this, dir));
        }
    }

    /**
     * Constructs an anonymous card containing the specified corners.
     * @param corners list of corners contained
     * @throws InvalidParameterException when a duplicate corner is found
     */
    protected PlaceableCard(List<Corner> corners) throws InvalidParameterException {
        this(null,corners);
    }

    /**
     * This constructor builds the card when positioned.
     * <p>
     *      It adds the information of the position. <br>
     *      This constructor exists in order to build immutable card objects.
     * </p>
     * @param placement coordinates at which it is placed
     * @param oldCard copied card
     */
    protected PlaceableCard(GamePoint placement, PlaceableCard oldCard){
        super(oldCard);
        this.corners = oldCard.corners;
        for (Corner c : corners.values()){
            c.setCardRef(this);
        }
        position = new GamePoint(placement);
    }

// ---- Corner related methods: they are already implemented because this information is visible and needs
//  to be visible at this depth in the inheritance tree. ----
    /**
     * Returns the corner in the selected direction.
     * @param cornDir indicates the selected corner direction;
     * @return the corner in the selected direction;
     */
    public Corner getCorner(CornerDirection cornDir){
        return corners.get(cornDir);
    }

    /**
     * Returns a list of the card's free corners.
     * <p>
     *     A corner is considered free when it is not covered or it doesn't
     *     cover another corner. <br>
     *     A filled corner is not free.
     * </p>
     * @return a list of free corners.
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
     * Returns a counting array of the resources in the visible corners of the card.
     * @return an array with the count of the card's visible resources
     */
    // Used in implementations to avoid code duplication

    protected int[] getCornerResources(){
        int[] resourcesCount = new int[7];

        Set<CornerDirection> cornerKeys = corners.keySet();
        for(CornerDirection cornDir: cornerKeys){
            // Get resource if visible
            GameResource res = corners.get(cornDir).getResource();
            if(res != null && res != GameResource.FILLED){
                resourcesCount[res.getResourceIndex()]++;
            }
        }
        return resourcesCount;
    }


    /**
     * Returns a map containing the number of visible resources in the card.
     * @return a map with the count of visible resources
     */
    abstract public Map<GameResource, Integer> getCardResources();

    /**
     * Returns the resource associated with the card colour.
     * <p>
     *     Each resource in the game has a colour. Therefore a resource
     *     can be used to identify the colour of the card.
     * </p>
     * @return the resource that identifies the colour of this card
     */
    abstract public GameResource getCardColour();

    /**
     * Getter for the position of the card.
     * @return the position of the card
     * @throws IllegalStateException when trying to access the position of a not positioned card.
     */
    public GamePoint getPosition() throws IllegalStateException {
        if(position == null){
            throw new IllegalStateException("Tried to access position on a card that wasn't placed");
        }
        return position;
    }

    /**
     * Sets the position of the card.
     * <p>
     *     In order to make a card an immutable object, this method build a new identical
     *     card with the only new information of position.
     * </p>
     * @param placement the position in which the card has to be placed
     * @return an equal positioned card
     */
    public abstract PlaceableCard setPosition(GamePoint placement);


    // OBJECT METHODS

    /**
     * This method compares two placeable card objects.
     * <p>
     *     Returns true if the two cards have the same properties:<br>
     *     - Orientation;<br>
     *     - Corners.<br>
     *     Returns false otherwise.
     * </p>
     * @param other the card with which to compare
     * @return true if the card is the same as the argument; false otherwise
     */
    protected boolean compare(PlaceableCard other){
        return compareCard(other) &&
                corners.equals(other.corners);
        // corners.equals delegates comparison to Corner.equals for each corner
    }

    /**
     * Compares two cards without considering corners.
     * @param other the card with which to compare
     * @return true if the card is the same as the argument, false otherwise
     */
    protected boolean compareCard(PlaceableCard other){
        return super.compareCard(other) &&
                (position == null || other.position == null || position.equals(other.position));
    }

    @Override
    public String toString() {
        return super.toString() +
                ((position != null) ? position + "\n" : "This card still isn't placed\n") +
                corners.toString() + "\n";
    }


    /**
     * This function converts a counting array of resources into a map.
     * All the information is preserved.
     * @param resourcesCount the counting array of resources
     * @return a map which keys are the resources and the values are the count of each.
     */
    public static Map<GameResource, Integer> resourceArrayToMap(int[] resourcesCount){
        Hashtable<GameResource, Integer> countedResources = new Hashtable<>();
        for (GameResource r : GameResource.values()){
            if(r != GameResource.FILLED)
                countedResources.put(r, resourcesCount[r.getResourceIndex()]);
        }

        return countedResources;
    }
}
