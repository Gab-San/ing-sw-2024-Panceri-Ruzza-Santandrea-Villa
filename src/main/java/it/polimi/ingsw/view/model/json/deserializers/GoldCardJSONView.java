package it.polimi.ingsw.view.model.json.deserializers;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.cards.ViewGoldCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        String strategyAsString = parseStrategyAsString(this.getPlacementCost());

        return new ViewGoldCard(cardId, imgFront, imgBack, corners, pointsOnPlace, backResource, placementCostList, strategyAsString);
    }

    private List<ViewCorner> convertCorners(List<CornerJView> cornerJS) {
        if (cornerJS == null) {
            return null;
        }
        return cornerJS.stream()
                .map(cornerJView -> new ViewCorner(
                        GameResource.getResourceFromName(cornerJView.getFrontResource()),
                        GameResource.getResourceFromName(cornerJView.getBackResource()),
                        CornerDirection.getDirectionFromString(cornerJView.getDirection())))
                .collect(Collectors.toList());
    }

    private List<GameResource> parsePlacementCost(String placementCost) {
        if (placementCost == null || placementCost.isEmpty()) {
            return List.of();
        }
        List<GameResource> resources = new ArrayList<>();
        String[] parts = placementCost.split(", ");
        for (String part : parts) {
            String[] splitPart = part.split("-");
            if (splitPart.length == 2) {
                String resourceStr = splitPart[1].trim();
                resources.add(GameResource.getResourceFromNameInitial(resourceStr));
            }
        }
        return resources;
    }

    private String parseStrategyAsString(String placementCost) {
        if (placementCost == null || placementCost.isEmpty()) {
            return "";
        }
        StringBuilder strategy = new StringBuilder();
        String[] parts = placementCost.split(", ");
        for (String part : parts) {
            String[] splitPart = part.split("-");
            if (splitPart.length == 2) {
                strategy.append(splitPart[1].trim());
            } else if (splitPart.length == 1 && Character.isLetter(part.trim().charAt(0))) {
                strategy.append(part.trim().charAt(0));
            }
        }
        return strategy.toString();
    }
}
