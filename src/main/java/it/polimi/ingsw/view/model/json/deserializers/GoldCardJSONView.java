package it.polimi.ingsw.view.model.json.deserializers;

import it.polimi.ingsw.GameResource;

import java.util.List;

public class GoldCardJSONView {
    private String cardId;
    private String imgFront;
    private String imgBack;
    private String placementCost;
    private String pointsOnPlace;
    private List<CornerJView> cornersJS;
    private GameResource backResource;

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

    public GameResource getBackResource() {
        return backResource;
    }
    public void setBackResource(GameResource backResource) {
        this.backResource = backResource;
    }

    public List<CornerJView> getCornersJS() {
        return cornersJS;
    }
    public void setCornersJS(List<CornerJView> cornersJS) {
        this.cornersJS = cornersJS;
    }

    public String getImgFront() {
        return imgFront;
    }
    public void setImgFront(String imgFront) {
        this.imgFront = imgFront;
    }

    public String getImgBack() {
        return imgBack;
    }
    public void setImgBack(String imgBack) {
        this.imgBack = imgBack;
    }
}