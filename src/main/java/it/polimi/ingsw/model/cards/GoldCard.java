package it.polimi.ingsw.model.cards;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.cards.cardstrategies.GoldCardStrategy;
import it.polimi.ingsw.GameResource;
import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class represents the gold card, which is mainly responsible for scoring points.
 * <p>
 *     A gold card is the second type of playable card.
 *     In addition to the corners and resources, gold cards always have placement points.
 * </p>
 * <p>
 *     What differentiates them mostly from resource cards is the placement cost.
 *     In order to place a gold card, enough resources as listed at the bottom of it must be visible in the
 *     play area
 * </p>
 * <p>
 *     By default when instantiated a card is facing downwards (displaying back)
 * </p>
 */
public class GoldCard extends PlayCard{
    private final Hashtable<GameResource, Integer> placementCost;
    private final GoldCardStrategy goldStrat;

    /**
     * Constructor for gold card.
     * <p>
     *     In addition to corners and the back resource, must specify points on place,
     *     gold scoring method and placement cost.
     * </p>
     * @param backResource the resource displayed on the back
     * @param pointsOnPlace points multiplier scored on placement
     * @param plCost a map defining the placement cost of the card
     * @param goldStrat method of points scoring
     * @param corners the corners attached to the card
     * @throws InvalidParameterException when a duplicate corner is found
     */
    public GoldCard(GameResource backResource, int pointsOnPlace, Map<GameResource, Integer> plCost,
                    GoldCardStrategy goldStrat, Corner... corners) throws InvalidParameterException {
        this(null, backResource, pointsOnPlace, plCost, goldStrat, corners);
    }

    /**
     * Constructs a gold card with the given identifier and specified back resource, placement points, placement cost,
     * gold strategy and corners.
     * @param cardID unique identifier
     * @param backResource resource displayed on the back
     * @param pointsOnPlace placement points
     * @param plCost placement cost
     * @param goldStrat gold strategy
     * @param corners card's corners
     * @throws InvalidParameterException when a duplicate corner is found
     */
    public GoldCard(String cardID, GameResource backResource, int pointsOnPlace, Map<GameResource, Integer> plCost,
                    GoldCardStrategy goldStrat, Corner... corners) throws InvalidParameterException {
        super(cardID, backResource, pointsOnPlace, corners);
        this.goldStrat = goldStrat;
        this.placementCost = new Hashtable<>();
        // Copying information from the constructed object to the card
        for(GameResource res: plCost.keySet()){
            this.placementCost.put(res, plCost.get(res));
        }
    }
    /**
     * Constructs a gold card with the given identifier and specified back resource, placement points, placement cost,
     * gold strategy and list of corners.
     * @param cardID unique identifier
     * @param backResource resource displayed on the back
     * @param pointsOnPlace placement points
     * @param plCost placement cost
     * @param goldStrat gold strategy
     * @param corners list of card's corners
     * @throws InvalidParameterException when a duplicate corner is found
     */
    public GoldCard(String cardID, GameResource backResource, int pointsOnPlace, Map<GameResource, Integer> plCost,
                    GoldCardStrategy goldStrat, List<Corner> corners) throws InvalidParameterException {
        super(cardID, backResource, pointsOnPlace, corners);
        this.goldStrat = goldStrat;
        this.placementCost = new Hashtable<>();
        // Copying information from the constructed object to the card
        for(GameResource res: plCost.keySet()){
            this.placementCost.put(res, plCost.get(res));
        }
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
    private GoldCard(GamePoint placement, GoldCard oldCard){
        super(placement, oldCard);
        this.placementCost = oldCard.placementCost;
        this.goldStrat = oldCard.goldStrat;
    }
    /**
     * Getter for the placement cost of the card. <br>
     * A gold card always has a placement cost that is displayed at the bottom of the card.
     * @return a table mapping the resources needed and the count of said resources.
     */
    @Override
    public Map<GameResource, Integer> getPlacementCost() {
        return isFaceUp ? placementCost : new Hashtable<>();
    }
    /**
     * Returns the points scored when placing this card.
     * @param playArea of the player who is currently making the action
     * @return the points received from the placement
     */
    @Override
    public int calculatePointsOnPlace(@NotNull PlayArea playArea){
        if(isFaceUp) {
            return pointsOnPlace * goldStrat.calculateSolves(playArea, this);
        }

        return 0;
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
    @Override
    public PlaceableCard setPosition(GamePoint placement) {
        return new GoldCard(placement, this);
    }

    // OBJECT METHOD
    /**
     * Indicates whether some object has the same properties as this one
     * @param other the reference object which to compare
     * @return true if the object is the same as the argument; false otherwise
     */
    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if(!(other instanceof GoldCard)) return false;

        return compare((GoldCard) other);
    }

    /**
     * This method compares two gold card objects.
     * <p>
     *     Returns true if the two cards have the same properties:
     *     - Orientation;<br>
     *     - Corners;<br>
     *     - Back resource and colour;<br>
     *     - Placement points;<br>
     *     - Placement cost;<br>
     *     - Gold strategy.<br>
     *     Returns false otherwise.
     * </p>
     * @param other the card with which to compare
     * @return true if the card is the same as the argument; false otherwise
     */
    @Override
    public boolean compareCard(PlaceableCard other){
        if(!(other instanceof GoldCard)) return false;
        GoldCard cardToComp = (GoldCard) other;
        return super.compareCard(other) &&
                goldStrat.equals(cardToComp.goldStrat) &&
                placementCost.equals(cardToComp.placementCost);
    }
}