package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.Point;

import java.util.*;

public abstract class ViewPlaceableCard extends ViewCard{
    private final Map<CornerDirection, ViewCorner> corners;
    private Point position;

    public ViewPlaceableCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners) {
        super(cardID, imageFrontName, imageBackName);

        Map<CornerDirection, ViewCorner> thisCorners = new Hashtable<>();
        for(ViewCorner c : corners) {
            ViewCorner corner = new ViewCorner(c);
            corner.setCardRef(this);
            if(thisCorners.put(corner.getDirection(), corner) != null)
                throw new IllegalArgumentException("Corners with duplicate directions were passed in ViewPlaceableCard constructor");
        }

        this.corners = Collections.unmodifiableMap(thisCorners);
        this.position = null;
    }
    public ViewPlaceableCard(ViewPlaceableCard other){
        super(other);
        Map<CornerDirection, ViewCorner> thisCorners = new Hashtable<>();
        other.corners.values().forEach(
                c -> thisCorners.put(c.getDirection(), new ViewCorner(c))
        );
        thisCorners.values().forEach(c -> c.setCardRef(this));
        this.corners = Collections.unmodifiableMap(thisCorners);
    }

    abstract public GameResource getCardColour();
    public GameResource getCornerResource(CornerDirection dir){
        return corners.get(dir).getResource();
    }
    public ViewCorner getCorner(CornerDirection dir) {
        return corners.get(dir);
    }

    public synchronized void setPosition(Point position) {
        this.position = position;
    }
    public synchronized Point getPosition(){
        return position;
    }
}
