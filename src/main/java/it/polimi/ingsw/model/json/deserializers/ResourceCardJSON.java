package it.polimi.ingsw.model.json.deserializers;

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

    public String getBackResource() {
        return backResource;
    }

    void setBackResource(String backResource) {
        this.backResource = backResource;
    }

    public String getFrontImageFileName() {
        return frontImageFileName;
    }

    void setFrontImageFileName(String frontImageFileName) {
        this.frontImageFileName = frontImageFileName;
    }

    public String getBackImageFileName() {
        return backImageFileName;
    }

    void setBackImageFileName(String backImageFileName) {
        this.backImageFileName = backImageFileName;
    }
}
