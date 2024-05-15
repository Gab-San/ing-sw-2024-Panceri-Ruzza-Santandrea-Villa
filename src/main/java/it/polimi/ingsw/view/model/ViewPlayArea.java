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

    // we can represent other players in PlayArea as it's a very simple representation
    // the local player (using the client) will use ViewPlayerHand to see their own hand
    private boolean isConnected;
    private List<GameResource> cardsInHand;

    public ViewPlayArea() {
        this.cardMatrix = new Hashtable<>();
        this.visibleResources = new Hashtable<>();
        this.freeCorners = new LinkedList<>();
    }

    /**
     * Places a card at position, covering corners and handling freeCorners list <br>
     * This function trusts the server and does not check the validity of the placement
     * @param position point on which to place the card
     * @param card card to place
     */
    public void placeCard(Point position, ViewPlaceableCard card){
        cardMatrix.put(position, card);
        for(CornerDirection dir : CornerDirection.values()){
            Point dirPos = position.move(dir);
            ViewPlaceableCard dirCard = cardMatrix.get(dirPos);
            if(dirCard != null){
                dirCard.getCorner(dir.opposite()).cover();
                freeCorners.remove(dirCard.getCorner(dir.opposite()));
            }
            else if(card.getCornerResource(dir) != GameResource.FILLED)
                freeCorners.add(card.getCorner(dir));
        }
    }

    public Map<Point, ViewPlaceableCard> getCardMatrix(){
        return Collections.unmodifiableMap(cardMatrix);
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

}
