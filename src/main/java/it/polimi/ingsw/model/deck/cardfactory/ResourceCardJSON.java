package it.polimi.ingsw.model.deck.cardfactory;

import java.util.List;

public class ResourceCardJSON {
    private String cardId;
    private int pointsOnPlace;
    private List<CornerJ> cornerJS;
    private String backResource;
    private String frontImageFileName;
    private String backImageFileName;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getPointsOnPlace() {
        return pointsOnPlace;
    }

    public void setPointsOnPlace(int pointsOnPlace) {
        this.pointsOnPlace = pointsOnPlace;
    }

    public List<CornerJ> getCornerJS() {
        return cornerJS;
    }

    public void setCornerJS(List<CornerJ> cornerJS) {
        this.cornerJS = cornerJS;
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
