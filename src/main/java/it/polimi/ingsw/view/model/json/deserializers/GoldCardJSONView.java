package it.polimi.ingsw.view.model.json.deserializers;

import it.polimi.ingsw.model.json.deserializers.CornerJ;
import it.polimi.ingsw.view.model.enums.GameResourceView;

import java.util.List;

public class GoldCardJSONView {
    private String cardId;
    private String placementCost;
    private String pointsOnPlace;
    private List<CornerJView> cornersJS;
    private GameResourceView backResource;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }


    public String getPlacementCost() {
        return placementCost;
    }

    public void setPlacementCost(String placementCost) {
        this.placementCost = placementCost;
    }

    public String getPointsOnPlace() {
        return pointsOnPlace;
    }

    public void setPointsOnPlace(String pointsOnPlace) {
        this.pointsOnPlace = pointsOnPlace;
    }

    public GameResourceView getBackResource() {
        return backResource;
    }

    public void setBackResource(GameResourceView backResource) {
        this.backResource = backResource;
    }

    public List<CornerJView> getCornersJS() {
        return cornersJS;
    }

    public void setCornersJS(List<CornerJView> cornersJS) {
        this.cornersJS = cornersJS;
    }
}