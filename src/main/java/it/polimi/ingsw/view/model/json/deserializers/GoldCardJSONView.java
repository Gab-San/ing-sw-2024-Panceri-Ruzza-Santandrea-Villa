package it.polimi.ingsw.view.model.json.deserializers;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.cards.ViewGoldCard;
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

    public ViewGoldCard toViewGoldCard() {
        String cardId = this.getCardId();
        String imgFront = this.getImgFront();
        String imgBack = this.getImgBack();
        int pointsOnPlace = Integer.parseInt(this.getPointsOnPlace());
        List<ViewCorner> corners = convertCorners(this.getCornersJS());
        GameResource backResource = this.getBackResource();
        List<GameResource> placementCostList = parsePlacementCost(this.getPlacementCost());
        String strategyAsString = ""; // TODO

        return new ViewGoldCard(cardId, imgFront, imgBack, corners, pointsOnPlace, backResource, placementCostList, strategyAsString);
    }

    private List<ViewCorner> convertCorners(List<CornerJView> cornersJS) {
        if (cornersJS == null) {
            return null;
        }
        return List.of(); //TODO
    }

    private List<GameResource> parsePlacementCost(String placementCost) {
        // TODO
        return null;
    }
}