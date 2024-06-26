package it.polimi.ingsw.model.json.deserializers;

/**
 * This class represents a serializable lightweight version of a corner
 * with the skeletal structure defined in the json files.
 */
public class CornerJ {
    private String direction;
    private String frontResource;
    private String backResource;

    /**
     * Default constructor.
     */
    public CornerJ(){
        direction = null;
        frontResource = null;
        backResource = null;
    }

    /**
     * Returns the relative direction to the center of the card in which the corner is placed.
     * @return direction of the corner
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the relative direction to the center of the card in which the corner is placed.
     * @param direction direction of the corner
     */
    void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Returns the resource displayed on the back of the corner.
     * @return back corner resource
     */
    public String getBackResource() {
        return backResource;
    }

    /**
     * Sets the resource displayed on the back of the corner.
     * @param backResource back corner resource
     */
    void setBackResource(String backResource) {
        this.backResource = backResource;
    }

    /**
     * Return the resource displayed on the front of the corner.
     * @return front corner resource
     */
    public String getFrontResource() {
        return frontResource;
    }

    /**
     * Sets the resource displayed on the front of the corner.
     * @param frontResource front corner resource
     */
    void setFrontResource(String frontResource) {
        this.frontResource = frontResource;
    }
}
