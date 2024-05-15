package it.polimi.ingsw.view;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.model.json.deserializers.CornerJ;
import it.polimi.ingsw.model.json.deserializers.PointOnPlace;

import java.util.List;
import java.util.Map;

public class GoldCardJSONView {
    private String cardId;
    private String colour;
    private Map<String, Integer> placementCost;
    private PointOnPlace pointsOnPlace;
    private List<CornerJ> cornersJS;
    private GameResource backResource;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Map<String, Integer> getPlacementCost() {
        return placementCost;
    }

    public void setPlacementCost(Map<String, Integer> placementCost) {
        this.placementCost = placementCost;
    }

    public PointOnPlace getPointsOnPlace() {
        return pointsOnPlace;
    }

    public void setPointsOnPlace(PointOnPlace pointsOnPlace) {
        this.pointsOnPlace = pointsOnPlace;
    }

    public List<CornerJ> getCornersJS() {
        return cornersJS;
    }

    public void setCornersJS(List<CornerJ> cornersJS) {
        this.cornersJS = cornersJS;
    }

    public GameResource getBackResource() {
        return backResource;
    }

    public void setBackResource(GameResource backResource) {
        this.backResource = backResource;
    }
    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}