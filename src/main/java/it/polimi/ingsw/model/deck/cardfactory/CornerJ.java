package it.polimi.ingsw.model.deck.cardfactory;


class CornerJ {
    private String direction;
    private String frontResource;
    private String backResource;

    String getDirection() {
        return direction;
    }

    void setDirection(String direction) {
        this.direction = direction;
    }

    public String getBackResource() {
        return backResource;
    }

    public void setBackResource(String backResource) {
        this.backResource = backResource;
    }

    public String getFrontResource() {
        return frontResource;
    }

    public void setFrontResource(String frontResource) {
        this.frontResource = frontResource;
    }
}
