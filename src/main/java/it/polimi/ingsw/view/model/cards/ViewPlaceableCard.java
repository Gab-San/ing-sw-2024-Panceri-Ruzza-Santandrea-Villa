package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.Point;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public abstract class ViewPlaceableCard extends ViewCard{
    Map<CornerDirection, ViewCorner> corners;
    Point position;

    public ViewPlaceableCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners) {
        super(cardID, imageFrontName, imageBackName);
        this.corners = new Hashtable<>();
        position = null;
        for(ViewCorner c : corners){
            ViewCorner corner = new ViewCorner(c);
            corner.setCardRef(this);
            if(this.corners.put(corner.getDirection(), corner) != null)
                throw new IllegalArgumentException("Corners with duplicate directions were passed in ViewPlaceableCard constructor");
        }
    }

    abstract public GameResource getCardColour();
    public GameResource getCornerResource(CornerDirection dir){
        return corners.get(dir).getResource();
    }
    public ViewCorner getCorner(CornerDirection dir) {
        return corners.get(dir);
    }

    public void setPosition(Point position) {
        this.position = position;
    }
    public Point getPosition(){
        return position;
    }
}
