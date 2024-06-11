package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The gold card representation in the ViewModel
 */
public class ViewGoldCard extends ViewPlayCard{
    /**
     * list of resources that are required to place the gold card
     */
    private final List<GameResource> placementCost;
    /**
     * string representation of the gold card strategy
     */
    private final String strategyAsString;

    /**
     * Constructs the gold card.
     * @param cardID this card's ID
     * @param imageFrontName the front image file name
     * @param imageBackName the back image file name
     * @param corners list of corners (not attached to another card)
     * @param pointsOnPlace points gained per strategy-solve on card placement
     * @param strategyAsString string representation of the gold card strategy
     * @param backResource the resource on the back of the card (also serves as card color)
     * @param placementCost list of resources that are required to place the gold card
     */
    public ViewGoldCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners,
                        int pointsOnPlace, GameResource backResource, List<GameResource> placementCost, String strategyAsString) {

        super(cardID, imageFrontName, imageBackName, corners, pointsOnPlace, backResource);
        this.placementCost = placementCost.stream().sorted(Comparator.comparingInt(GameResource::getResourceIndex)).toList(); // unmodifiable == thread-safe
        this.strategyAsString = strategyAsString;
    }
    /**
     * Construct the gold card as a copy of another gold card.
     * @param other the other gold card to copy.
     */
    public ViewGoldCard(ViewGoldCard other){
        super(other);
        this.placementCost = other.placementCost;
        this.strategyAsString = other.strategyAsString;
    }

    /**
     * @return An empty string if the card is face-down, or
     *        the pointsOnPlace joined with strategyAsString
     *        as a single string if the card is face-up.
     */
    @Override
    public String getPointsOnPlaceAsString() {
        if(!isFaceUp() || pointsOnPlace <= 0) return "";
        if(strategyAsString.isEmpty()) return super.getPointsOnPlaceAsString();
        else return super.getPointsOnPlaceAsString() + " | " + strategyAsString;
    }

    @Override
    public String getPlacementCostAsString(){
        if(!isFaceUp()) return "";
        else return placementCost.stream()
                .map(GameResource::toString)
                .collect(Collectors.joining());
    }

    @Override
    public List<GameResource> getPlacementCost() {
        return isFaceUp ? placementCost : new LinkedList<>();
    }

}
