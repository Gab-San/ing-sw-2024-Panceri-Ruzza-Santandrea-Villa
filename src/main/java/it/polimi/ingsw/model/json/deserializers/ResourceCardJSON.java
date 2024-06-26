package it.polimi.ingsw.model.json.deserializers;

import it.polimi.ingsw.GameResource;

import java.util.List;

/**
 * This class implements a lightweight version of the gold card as defined in the
 * json file.
 */
public class ResourceCardJSON {
    private String cardId;
    private int pointsOnPlace;
    private List<CornerJ> cornerJS;
    private GameResource backResource;

    /**
     * Returns the card identifier.
     * @return card identifier
     */
    public String getCardId() {
        return cardId;
    }
    /**
     * Sets the card identifier.
     * @param cardId card identifier
     */
    void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * Returns the placement points value.
     * @return placement points
     */
    public int getPointsOnPlace() {
        return pointsOnPlace;
    }

    /**
     * Sets the placement points value.
     * @param pointsOnPlace placement points value
     */
    void setPointsOnPlace(int pointsOnPlace) {
        this.pointsOnPlace = pointsOnPlace;
    }

    /**
     * Returns the list of lightweight corners contained in the card.
     * @return list of card's corners
     */
    public List<CornerJ> getCornerJS() {
        return cornerJS;
    }

    /**
     * Sets the list of lightweight corners contained in the card.
     * @param cornerJS list of card's corners
     */
    void setCornerJS(List<CornerJ> cornerJS) {
        this.cornerJS = cornerJS;
    }

    /**
     * Returns the resource displayed on the back of the card.
     * @return back card resource
     */
    public GameResource getBackResource() {
        return backResource;
    }

    /**
     * Sets the resource displayed on the back of the card.
     * @param backResource back card resource
     */
    void setBackResource(GameResource backResource) {
        this.backResource = backResource;
    }

}
