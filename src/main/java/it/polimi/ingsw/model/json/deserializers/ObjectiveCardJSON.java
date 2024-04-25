package it.polimi.ingsw.model.json.deserializers;

public class ObjectiveCardJSON {
    private String cardId;
    private String frontImageFileName;
    private String backImageFileName;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
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