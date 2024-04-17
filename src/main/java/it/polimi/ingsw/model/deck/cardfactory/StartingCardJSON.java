package it.polimi.ingsw.model.deck.cardfactory;

import java.util.List;

public class StartingCardJSON {
    private String cardId;
    private List<String> centralFrontResources;
    private List<CornerJ> cornerJS;
    private String frontImageFileName;
    private String backImageFileName;

    String getCardId() {
        return cardId;
    }

    void setCardId(String cardId) {
        this.cardId = cardId;
    }

    List<CornerJ> getCorners() {
        return cornerJS;
    }

    void setCorners(List<CornerJ> cornerJS) {
        this.cornerJS = cornerJS;
    }

    String getFrontImageFileName() {
        return frontImageFileName;
    }

    void setFrontImageFileName(String frontImageFileName) {
        this.frontImageFileName = frontImageFileName;
    }

    String getBackImageFileName() {
        return backImageFileName;
    }

    void setBackImageFileName(String backImageFileName) {
        this.backImageFileName = backImageFileName;
    }


    public List<String> getCentralFrontResources() {
        return centralFrontResources;
    }

    public void setCentralFrontResources(List<String> centralFrontResources) {
        this.centralFrontResources = centralFrontResources;
    }
}