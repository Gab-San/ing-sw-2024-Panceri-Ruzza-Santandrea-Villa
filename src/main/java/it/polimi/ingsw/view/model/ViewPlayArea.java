package it.polimi.ingsw.view.model;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.GameResource;

import java.util.*;

public class ViewPlayArea {
    private final Map<Point, ViewPlaceableCard> cardMatrix;
    private final Map<GameResource, Integer> visibleResources;
    private final List<ViewCorner> freeCorners;
    private GameResource color;

    public ViewPlayArea() {
        this.cardMatrix = Collections.synchronizedMap(new Hashtable<>());
        this.visibleResources = Collections.synchronizedMap(new Hashtable<>());
        this.freeCorners = Collections.synchronizedList(new LinkedList<>());
        color = null;
    }
    /**
     * Places a card at position (0,0), handling freeCorners list <br>
     * @param startCard starting card to place
     */
    public void placeStarting(ViewStartCard startCard){
        Point zero = new Point(0,0);
        cardMatrix.put(zero, startCard);
        startCard.setPosition(zero);
        for(CornerDirection dir : CornerDirection.values()) {
            if(startCard.getCorner(dir).getResource() != GameResource.FILLED)
                freeCorners.add(startCard.getCorner(dir));
        }
    }

    /**
     * Places a card at position, covering corners and handling freeCorners list <br>
     * This function trusts the server and does not check the validity of the placement
     * @param position point on which to place the card
     * @param card card to place
     */
    public void placeCard(Point position, ViewPlaceableCard card){
        cardMatrix.put(position, card);
        card.setPosition(position);
        for(CornerDirection dir : CornerDirection.values()){
            ViewPlaceableCard dirCard = cardMatrix.get(position.move(dir));
            if(dirCard != null){
                dirCard.getCorner(dir.opposite()).cover();
                freeCorners.remove(dirCard.getCorner(dir.opposite()));
            }
            else if(card.getCornerResource(dir) != GameResource.FILLED)
                freeCorners.add(card.getCorner(dir));
        }
    }

    public Map<Point, ViewPlaceableCard> getCardMatrix() {
        return Collections.unmodifiableMap(cardMatrix);
    }
    public ViewPlaceableCard getCardAt(Point position){
        return cardMatrix.get(position);
    }
    public ViewPlaceableCard getCardAt(int row, int col){
        return getCardAt(new Point(row, col));
    }

    public void setVisibleResources(Map<GameResource, Integer> visibleResources){
        this.visibleResources.clear();
        this.visibleResources.putAll(visibleResources);
    }
    public Map<GameResource, Integer> getVisibleResources(){
        return Collections.unmodifiableMap(visibleResources);
    }
    public List<ViewCorner> getFreeCorners(){
        return Collections.unmodifiableList(freeCorners);
    }

    public synchronized GameResource getColor() {
        return color;
    }
    public synchronized void setColor(GameResource color) {
        this.color = color;
    }
}
