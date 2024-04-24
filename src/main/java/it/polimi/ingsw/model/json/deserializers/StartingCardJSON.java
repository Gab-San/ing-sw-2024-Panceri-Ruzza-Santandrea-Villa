package it.polimi.ingsw.model.json.deserializers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using= StartingCardDeserializer.class)
public class StartingCardJSON {
    private String cardId;
    private List<String> centralFrontResources;
    private List<CornerJ> corners;
    private String frontImageFileName;
    private String backImageFileName;

    public String getCardId() {
        return cardId;
    }

    void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public List<CornerJ> getCorners() {
        return corners;
    }

    void setCorners(List<CornerJ> corners) {
        this.corners = corners;
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


    public List<String> getCentralFrontResources() {
        return centralFrontResources;
    }

    void setCentralFrontResources(List<String> centralFrontResources) {
        this.centralFrontResources = centralFrontResources;
    }
}