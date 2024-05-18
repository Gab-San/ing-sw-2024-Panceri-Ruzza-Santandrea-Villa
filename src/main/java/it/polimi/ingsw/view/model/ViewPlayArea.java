package it.polimi.ingsw.view.model;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.GameResource;

import static it.polimi.ingsw.GameResource.FILLED;

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
            if(startCard.getCorner(dir).getResource() != FILLED)
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
            else{
                if(card.getCornerResource(dir) != FILLED){
                    boolean isCornerFree = true;
                    Point pointToCheck = position.move(dir);
                    for(CornerDirection dir2 : CornerDirection.values()) {
                        ViewPlaceableCard cardDir = cardMatrix.get(pointToCheck.move(dir2));
                        if (cardDir != null && cardDir.getCornerResource(dir2.opposite()) == FILLED) {
                            isCornerFree = false;
                            break;
                        }
                    }
                    if(isCornerFree) freeCorners.add(card.getCorner(dir));
                }
                else{    // if that corner is FILLED, then check if it's blocking placement on another freeCorner
                    Point pointLocked = position.move(dir);
                    for(CornerDirection dir2 : CornerDirection.values()){
                        ViewPlaceableCard cardDir = cardMatrix.get(pointLocked.move(dir2));
                        if(cardDir != null) {
                            ViewCorner possibleLockedCorner = cardDir.getCorner(dir2.opposite());
                            if (possibleLockedCorner.getResource() != FILLED)
                                freeCorners.remove(possibleLockedCorner);
                        }
                    }
                }
            }
        }
    }

    private int countResource(GameResource res, String placementCostString){
        if(placementCostString.isEmpty()) return 0;
        return (int) Arrays.stream(placementCostString.split(""))
                .filter(s -> s.equals(res.toString()))
                .count();
    }
    public boolean validatePlacement(Point position, ViewPlayCard playCard){
        if(cardMatrix.get(position) != null) return false;

        // checks valid cost before placing
        for(GameResource res : GameResource.values()){
            if (visibleResources.get(res) == null || visibleResources.get(res) < countResource(res, playCard.getPlacementCostAsString()))
                return false;
        }
        // checks for FILLED corners, invalid placement if it would cover any
        for(CornerDirection dir : CornerDirection.values()){
            ViewPlaceableCard dirCard = cardMatrix.get(position.move(dir));
            if(dirCard != null && dirCard.getCornerResource(dir.opposite()) == FILLED)
                return false;
        }

        return true;
    }

    public ViewPlaceableCard getCardAt(Point position){
        return cardMatrix.get(position);
    }
    public ViewPlaceableCard getCardAt(int row, int col){
        return cardMatrix.get(new Point(row, col));
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
