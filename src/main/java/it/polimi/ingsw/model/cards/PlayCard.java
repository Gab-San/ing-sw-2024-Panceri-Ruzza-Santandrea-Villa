package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.functions.UsefulFunc;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class represents a card that can be played.
 * <p>
 *     A play card (short for playable) is a card that is actively played through the main stage
 *     of the game. All of the cards that can be held in the hand and then played during a player's turn
 *     must be play cards.
 * </p>
 *  <p>
 *       By default when instantiated a card is facing downwards (displaying back)
 *  </p>
 */
public abstract class PlayCard extends PlaceableCard{
    private final GameResource backResource;
    /**
     * Points scored on card's placement
     */
    protected final int pointsOnPlace;

    /**
     * Default constructor: builds a "blank" card with all corners empty on both sides.
     * Also doesn't initialize the resource and sets the points on placement to zero.
     */
    protected PlayCard() {
        super();
        backResource = null;
        pointsOnPlace = 0;
    }

    /**
     * This constructor creates a card with no points on placement.
     * @param backResource the resource displayed on the back of the card
     * @param corners the corners associated with the card
     */
    protected PlayCard(GameResource backResource, Corner... corners){
        super(corners);
        this.backResource = backResource;
        this.pointsOnPlace = 0;
    }

    /**
     * This constructor creates a card with placement points.
     * @param backResource the resource displayed on the back of the card
     * @param pointsOnPlace the points multiplier on card placing
     * @param corners the corners associated with the card
     * @throws InvalidParameterException when a duplicate corner is found
     */
    protected PlayCard(GameResource backResource, int pointsOnPlace, Corner... corners) throws InvalidParameterException {
        super(corners);
        this.backResource = backResource;
        this.pointsOnPlace = pointsOnPlace;
    }

    protected PlayCard(GameResource backResource, int pointsOnPlace, List<Corner> corners) throws InvalidParameterException {
        super(corners);
        this.backResource = backResource;
        this.pointsOnPlace = pointsOnPlace;
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
    protected PlayCard(Point placement, PlayCard oldCard){
        super(placement, oldCard);
        this.backResource = oldCard.backResource;
        this.pointsOnPlace = oldCard.pointsOnPlace;
    }

    /**
     * Returns a map containing the number of visible resources on the card.<br>
     * These are either visible corner resources or  card's back resource.
     * @return a map with the count of the card's visible resources
     */
    @Override
    public Map<GameResource, Integer> getCardResources() {

        if(!isFaceUp){
            int[] resourcesCount = new int[7];
            if(backResource != null && backResource != GameResource.FILLED)
                resourcesCount[backResource.getResourceIndex()]++;
            return UsefulFunc.resourceArrayToMap(resourcesCount);
        }

        int[] resourcesCount = super.getCornerResources();
        return UsefulFunc.resourceArrayToMap(resourcesCount);
    }

    /**
     * Returns the resource corresponding to the colour of the card.
     * @return color's corresponding resource
     */
    @Override
    public GameResource getCardColour() {
        return backResource;
    }

    /**
     * Getter for the placement cost of the card.
     * @return a table mapping the resources needed and the count of said resources.
     */
    public abstract Map<GameResource, Integer> getPlacementCost();

    /**
     * Returns the points scored when placing this card.
     * @param playArea of the player who is currently making the action
     * @return the points received from the placement
     */
    public abstract int calculatePointsOnPlace(PlayArea playArea);

    // OBJECT METHODS
    /**
     * Indicates whether some object has the same properties as this one
     * @param other the reference object which to compare
     * @return true if the object is the same as the argument; false otherwise
     */
    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if(!(other instanceof PlayCard)) return false;

        return compare((PlayCard) other);
    }

    /**
     * This method compares two playable card objects.
     * <p>
     *     Returns true if the two cards have the same properties:<br>
     *     - Orientation;<br>
     *     - Corners;<br>
     *     - Back resource and colour;<br>
     *     - Placement points;<br>
     *     - Placement cost.<br>
     *     Returns false otherwise.
     * </p>
     * @param other the card with which to compare
     * @return true if the card is the same as the argument; false otherwise
     */
    @Override
    protected boolean compareCard(PlaceableCard other){
        if(!(other instanceof PlayCard)) return false;
        PlayCard cardToComp = (PlayCard) other;
        return super.compareCard(other) &&
                getCardColour() == cardToComp.getCardColour() &&
                pointsOnPlace == cardToComp.pointsOnPlace;
    }

}
