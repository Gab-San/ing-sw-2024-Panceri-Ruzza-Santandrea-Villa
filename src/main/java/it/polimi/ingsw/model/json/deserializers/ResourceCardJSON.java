package it.polimi.ingsw.model.json.deserializers;

import it.polimi.ingsw.GameResource;

import java.util.List;

public class ResourceCardJSON {
    private String cardId;
    private int pointsOnPlace;
    private List<CornerJ> cornerJS;
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

    public List<CornerJ> getCornerJS() {
        return cornerJS;
    }

    void setCornerJS(List<CornerJ> cornerJS) {
        this.cornerJS = cornerJS;
    }

    public GameResource getBackResource() {
        return backResource;
    }

    void setBackResource(GameResource backResource) {
        this.backResource = backResource;
    }

}
