package it.polimi.ingsw.model.json.deserializers;


public class CornerJ {
    private String direction;
    private String frontResource;
    private String backResource;

    public String getDirection() {
        return direction;
    }

    void setDirection(String direction) {
        this.direction = direction;
    }

    public String getBackResource() {
        return backResource;
    }

    void setBackResource(String backResource) {
        this.backResource = backResource;
    }

    public String getFrontResource() {
        return frontResource;
    }

    void setFrontResource(String frontResource) {
        this.frontResource = frontResource;
    }
}
