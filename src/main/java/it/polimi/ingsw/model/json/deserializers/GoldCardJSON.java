package it.polimi.ingsw.model.json.deserializers;

import java.util.List;

public class GoldCardJSON {
    private String cardId;
    private List<PlacementCost> placementCost;
    private List<PointOnPlace> pointsOnPlace;
    private List<CornerJ> cornersJS;
    private String backResource;
    private String frontImageFileName;
    private String backImageFileName;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public List<PlacementCost> getPlacementCost() {
        return placementCost;
    }

    public void setPlacementCost(List<PlacementCost> placementCost) {
        this.placementCost = placementCost;
    }

    public List<PointOnPlace> getPointsOnPlace() {
        return pointsOnPlace;
    }

    public void setPointsOnPlace(List<PointOnPlace> pointsOnPlace) {
        this.pointsOnPlace = pointsOnPlace;
    }

    public List<CornerJ> getCornersJS() {
        return cornersJS;
    }

    public void setCornersJS(List<CornerJ> cornersJS) {
        this.cornersJS = cornersJS;
    }

    public String getBackResource() {
        return backResource;
    }

    public void setBackResource(String backResource) {
        this.backResource = backResource;
    }

    public String getFrontImageFileName() {
        return frontImageFileName;
    }

    public void setFrontImageFileName(String frontImageFileName) {
        this.frontImageFileName = frontImageFileName;
    }

    public String getBackImageFileName() {
        return backImageFileName;
    }

    public void setBackImageFileName(String backImageFileName) {
        this.backImageFileName = backImageFileName;
    }
}