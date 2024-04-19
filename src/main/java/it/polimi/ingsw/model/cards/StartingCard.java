package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.functions.UsefulFunc;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * This represents the starting card: the first card placed in the setup phase.
 * <p>
 *     Starting card can store multiple resources in the center of the card.
 *     They can also display a resource on both sides of a corner.
 * </p>
 * <p>
 *     By default when instantiated a card is facing downwards (displaying back)
 * </p>
 */
public class StartingCard extends PlaceableCard {
    private final List<GameResource> centralFrontResources;

    /**
     * Constructs a starting card filled with the required information.
     * @param centralRes The resources shown in the center of the card
     * @param corners The corners of the card
     */
    public StartingCard(GameResource[] centralRes, Corner... corners) throws InvalidParameterException {
        super(corners);

        this.centralFrontResources = new ArrayList<>();
        centralFrontResources.addAll(Arrays.asList(centralRes));
    }

    /**
     * Constructs a starting card from another one, adding the placement information.
     * @param placement The placement point of the card (starting cards are always placed at (0,0))
     * @param oldCard The unplaced card which is copied
     */
     private StartingCard(Point placement, StartingCard oldCard) {
        super(placement, oldCard);
        this.centralFrontResources = oldCard.centralFrontResources;
    }

    /**
     * Returns a map containing the number of visible resources on the card.<br>
     * These are either front corner resources with the card central resources or back corner resources.
     * @return a map with the count of the card's visible resources
     */
    @Override
    public Map<GameResource, Integer> getCardResources() {
        int[] resourcesCount = super.getCornerResources();
        if(isFaceUp) {
            for(GameResource res: centralFrontResources){
                resourcesCount[res.getResourceIndex()]++;
            }
        }

        return UsefulFunc.resourceArrayToMap(resourcesCount);
    }

    /**
     * Starting cards are white cards. Since no objective has a use for white cards
     * their colour is set to null.
     * @return null (associated with white colour)
     */
    @Override
    public GameResource getCardColour() {
        return null;
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
        return new StartingCard(placement, this);
    }

    // OBJECT METHODS
    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if(!(other instanceof StartingCard)) return false;
        return compare((StartingCard) other);
    }
    @Override
    public boolean compareCard(PlaceableCard other){
        StartingCard cardToComp = (StartingCard) other;
        return super.compareCard(other) &&
                centralFrontResources.equals(cardToComp.centralFrontResources);
    }
}
