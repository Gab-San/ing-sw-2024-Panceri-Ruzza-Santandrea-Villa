package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

import java.util.List;

/**
 * The base class of PlayCards in the ViewModel
 */
public abstract class ViewPlayCard extends ViewPlaceableCard{
    /**
     * Points score when placing the card.
     */
    protected final int pointsOnPlace;
    private final GameResource backResource;

    /**
     * Constructs the play card base.
     * @param cardID this card's ID
     * @param imageFrontName the front image file name
     * @param imageBackName the back image file name
     * @param corners list of corners (not attached to another card)
     * @param pointsOnPlace points gained on card placement
     * @param backResource the resource on the back of the card (also serves as card color)
     */
    public ViewPlayCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners, int pointsOnPlace, GameResource backResource) {
        super(cardID, imageFrontName, imageBackName, corners);
        this.pointsOnPlace = pointsOnPlace;
        this.backResource = backResource;
    }
    /**
     * Construct the play card as a copy of another play card.
     * @param other the other play card to copy.
     */
    public ViewPlayCard(ViewPlayCard other){
        super(other);
        this.pointsOnPlace = other.pointsOnPlace;
        this.backResource = other.backResource;
    }

    /**
     * @return the resource on the back of this card.
     *        That resource is associated with the card's color.
     */
    @Override
    public GameResource getCardColour() {
        return backResource;
    }

    /**
     * @return An empty string if the card is face-down, or
     *        the pointsOnPlace as a string if the card is face-up.
     */
    public String getPointsOnPlaceAsString() {
        if(!isFaceUp()) return "";
        else return pointsOnPlace > 0 ? Integer.toString(pointsOnPlace) : "";
    }

    /**
     * @return An empty string if the card is face-down, or
     *        the placement cost as a string of resource initials
     *        if the card is face-up.
     */
    abstract public String getPlacementCostAsString();

    /**
     * @return a list of resources that are required to place this card
     */
    abstract public List<GameResource> getPlacementCost();
}
