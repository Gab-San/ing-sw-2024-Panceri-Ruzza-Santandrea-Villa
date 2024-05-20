package it.polimi.ingsw.view.model.json.deserializers;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.model.json.deserializers.CornerJ;

import java.util.List;

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
}
