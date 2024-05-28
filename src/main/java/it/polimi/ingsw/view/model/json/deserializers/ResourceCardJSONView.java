package it.polimi.ingsw.view.model.json.deserializers;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.cards.ViewResourceCard;

import java.util.List;
import java.util.stream.Collectors;

public class ResourceCardJSONView {
    private String cardId;
    private String imgFront;
    private String imgBack;
    private int pointsOnPlace;
    private List<CornerJView> cornerJS;
    private GameResource backResource;

    public String getCardId() {
        return cardId;
    }

    void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getPointsOnPlace() {
        return pointsOnPlace;
    }
    void setPointsOnPlace(int pointsOnPlace) {
        this.pointsOnPlace = pointsOnPlace;
    }

    public List<CornerJView> getCornerJS() {
        return cornerJS;
    }
    void setCornerJS(List<CornerJView> cornerJS) {
        this.cornerJS = cornerJS;
    }

    public GameResource getBackResource() {
        return backResource;
    }
    void setBackResource(GameResource backResource) {
        this.backResource = backResource;
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

    public ViewResourceCard toViewResourceCard() {
        String cardId = this.getCardId();
        String imgFront = this.getImgFront();
        String imgBack = this.getImgBack();
        int pointsOnPlace = this.getPointsOnPlace();
        List<ViewCorner> corners = convertCorners(this.getCornerJS());
        GameResource backResource = this.getBackResource();

        return new ViewResourceCard(cardId, imgFront, imgBack, corners, pointsOnPlace, backResource);
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
}
