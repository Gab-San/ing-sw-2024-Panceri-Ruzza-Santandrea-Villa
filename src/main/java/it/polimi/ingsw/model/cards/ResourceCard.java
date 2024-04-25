package it.polimi.ingsw.model.cards;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.PlayArea;
import org.jetbrains.annotations.Nullable;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class represents the resource card
 * <p>
 *     The resource card is one of the two playable card types.
 *     It is a card composed of the corners and a back resource. <br>
 *     It has no placement cost but it may have placement points.
 * </p>
 * <p>
 *      By default when instantiated a card is facing downwards (displaying back)
 * </p>
 */
public class ResourceCard extends PlayCard{
    /**
     * Default constructor: builds a "blank" card with all corners empty on both sides.
     * Also doesn't initialize the resource and sets the points on placement to zero.
     */
    public ResourceCard() {
        super();
    }

    /**
     * Constructor for resource card with no points on place.
     * @param backResource the resource displayed on the back of the card
     * @param corners the corners associated with the card
     * @throws InvalidParameterException when duplicate of corner fount
     */
    public ResourceCard(GameResource backResource, Corner... corners) throws InvalidParameterException {
        super(backResource, corners);
    }

    /**
     * Constructor for resource card with placement points.
     * @param backResource the resource displayed on the back of the card
     * @param pointsOnPlace the placement points
     * @param corners the corners associated with the card
     * @throws InvalidParameterException when duplicate of corner found
     */
    public ResourceCard(GameResource backResource, int pointsOnPlace, Corner... corners) throws InvalidParameterException {
        super(backResource, pointsOnPlace, corners);
    }

    public ResourceCard(GameResource backResource, int pointsOnPlace, List<Corner> corners) throws InvalidParameterException {
        super(backResource, pointsOnPlace, corners);
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
    private ResourceCard(Point placement, ResourceCard oldCard){
        super(placement, oldCard);
    }

    /**
     * Returns the map of counted resource needed to place the card. <br>
     * Since the resource cards don't have a placement cost it returns an empty map.
     * @return empty map
     */
    @Override
    public Map<GameResource, Integer> getPlacementCost() {
        return new Hashtable<>();
    }

    /**
     * Returns the points scored when placing this card.
     * @param playArea of the player who is currently making the action
     * @return the points received from the placement
     */
    @Override
    public int calculatePointsOnPlace(@Nullable PlayArea playArea) {
        if(isFaceUp) {
            return pointsOnPlace;
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
    public PlaceableCard setPosition(Point placement) {
        return new ResourceCard(placement, this);
    }
}
