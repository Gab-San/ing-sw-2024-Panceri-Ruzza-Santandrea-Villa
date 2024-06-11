package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

import java.util.LinkedList;
import java.util.List;

/**
 * The resource card representation in the ViewModel
 */
public class ViewResourceCard extends ViewPlayCard {

    /**
     * Constructs the resource card.
     * @param cardID this card's ID
     * @param imageFrontName the front image file name
     * @param imageBackName the back image file name
     * @param corners list of corners (not attached to another card)
     * @param pointsOnPlace points gained on card placement
     * @param backResource the resource on the back of the card (also serves as card color)
     */
    public ViewResourceCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners, int pointsOnPlace, GameResource backResource) {
        super(cardID, imageFrontName, imageBackName, corners, pointsOnPlace, backResource);
    }
    /**
     * Construct the resource card as a copy of another resource card.
     * @param other the other resource card to copy.
     */
    public ViewResourceCard(ViewResourceCard other){
        super(other);
    }

    /**
     * @return An empty string since resource cards
     *         don't have a placement cost
     */
    @Override
    public String getPlacementCostAsString() {
        return "";
    }

    /**
     * @return An empty list since resource cards
     *         don't have a placement cost
     */
    @Override
    public List<GameResource> getPlacementCost() {
        return new LinkedList<>();
    }
}
