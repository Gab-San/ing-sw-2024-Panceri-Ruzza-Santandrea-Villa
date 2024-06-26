package it.polimi.ingsw.model.json.deserializers;

import it.polimi.ingsw.GameResource;

import java.util.List;
import java.util.Map;

/**
 * This class implements a lightweight version of the gold card as defined in the
 * json file.
 */
public class GoldCardJSON {
    private String cardId;
    private Map<String, Integer> placementCost;
    private PointOnPlace pointsOnPlace;
    private List<CornerJ> cornersJS;
    private GameResource backResource;

    /**
     * Returns the card identifier.
     * @return card identifier
     */
    public String getCardId() {
        return cardId;
    }

    /**
     * Sets the card identifier.
     * @param cardId card identifier
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * Returns the map describing the placement cost of the card.
     * @return placement cost map
     */
    public Map<String, Integer> getPlacementCost() {
        return placementCost;
    }

    /**
     * Sets the map describing the placement cost of the card.
     * @param placementCost placement cost map
     */
    public void setPlacementCost(Map<String, Integer> placementCost) {
        this.placementCost = placementCost;
    }

    /**
     * Returns the placement points value.
     * @return placement points
     */
    public PointOnPlace getPointsOnPlace() {
        return pointsOnPlace;
    }

    /**
     * Sets the placement points value.
     * @param pointsOnPlace placement points value
     */
    public void setPointsOnPlace(PointOnPlace pointsOnPlace) {
        this.pointsOnPlace = pointsOnPlace;
    }

    /**
     * Returns the list of lightweight corners contained in the card.
     * @return list of card's corners
     */
    public List<CornerJ> getCornersJS() {
        return cornersJS;
    }

    /**
     * Sets the list of lightweight corners contained in the card.
     * @param cornersJS list of card's corners
     */
    public void setCornersJS(List<CornerJ> cornersJS) {
        this.cornersJS = cornersJS;
    }

    /**
     * Returns the resource displayed on the back of the card.
     * @return back card resource
     */
    public GameResource getBackResource() {
        return backResource;
    }

    /**
     * Sets the resource displayed on the back of the card.
     * @param backResource back card resource
     */
    public void setBackResource(GameResource backResource) {
        this.backResource = backResource;
    }
}