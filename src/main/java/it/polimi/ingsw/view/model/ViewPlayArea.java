package it.polimi.ingsw.view.model;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.events.update.DisplayFreeCorners;
import it.polimi.ingsw.view.events.update.DisplayPlaceCard;
import it.polimi.ingsw.view.events.update.DisplayVisibleResources;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.util.*;

import static it.polimi.ingsw.GameResource.FILLED;

public class ViewPlayArea {
    private final String owner;
    private final Map<GamePoint, ViewPlaceableCard> cardMatrix;
    private final Map<GameResource, Integer> visibleResources;
    private final List<ViewCorner> freeCorners;
    private GameResource color;
    private final ViewBoard board;
    public ViewPlayArea(String owner, ViewBoard board) {
        this.owner = owner;
        this.board = board;
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
        GamePoint zero = new GamePoint(0,0);
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
    public void placeCard(GamePoint position, ViewPlaceableCard card){
        setCard(position, card);
        for(CornerDirection dir : CornerDirection.values()){
            ViewPlaceableCard dirCard = cardMatrix.get(position.move(dir));
            if(dirCard != null){
                dirCard.getCorner(dir.opposite()).cover();
                freeCorners.remove(dirCard.getCorner(dir.opposite()));
            }
            else{
                if(card.getCornerResource(dir) != FILLED){
                    boolean isCornerFree = true;
                    GamePoint pointToCheck = position.move(dir);
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
                    GamePoint pointLocked = position.move(dir);
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

        if(board.getPlayerHand().getNickname().equals(owner))
            board.notifyView(SceneID.getMyAreaSceneID(),
                    new DisplayPlaceCard(owner, true, cardMatrix));
        else
            board.notifyView(SceneID.getMyAreaSceneID(),
                    new DisplayPlaceCard(owner, true, cardMatrix));
    }

    private int countResource(GameResource res, String placementCostString){
        if(placementCostString.isEmpty()) return 0;
        return (int) Arrays.stream(placementCostString.split(""))
                .filter(s -> s.equals(res.toString()))
                .count();
    }
    public boolean validatePlacement(GamePoint position, ViewPlayCard playCard){
        if(cardMatrix.get(position) != null) return false;

        // checks valid cost before placing
        if(!playCard.getPlacementCost().isEmpty())
            for(GameResource res : GameResource.values()){
                int numOfResInCost = (int) playCard.getPlacementCost().stream().filter(r->r.equals(res)).count();
                if (visibleResources.getOrDefault(res, 0) < numOfResInCost)
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
    public GamePoint getPositionByID(String cardID) throws IllegalArgumentException{
        synchronized (cardMatrix){
            return cardMatrix.values().stream()
                    .filter(c->c.getCardID().equals(cardID))
                    .findFirst()
                    .orElseThrow(()->new IllegalArgumentException("Your playArea does not contain card "+ cardID))
                    .getPosition();
        }
    }

    public ViewPlaceableCard getCardAt(GamePoint position){
        return cardMatrix.get(position);
    }
    public ViewPlaceableCard getCardAt(int row, int col){
        return cardMatrix.get(new GamePoint(row, col));
    }
    public Map<GamePoint, ViewPlaceableCard> getCardMatrix(){ return Collections.unmodifiableMap(cardMatrix);}

    public void setVisibleResources(Map<GameResource, Integer> visibleResources){
        this.visibleResources.clear();
        this.visibleResources.putAll(visibleResources);
        if(board.getPlayerHand().getNickname().equals(owner))
            board.notifyView(SceneID.getMyAreaSceneID(),
                    new DisplayVisibleResources(owner, true, visibleResources));
        else
            board.notifyView(SceneID.getOpponentAreaSceneID(owner),
                    new DisplayVisibleResources(owner, false, visibleResources));
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

    public void setCard(GamePoint position, ViewPlaceableCard card) {
        cardMatrix.put(position, card);
        card.setPosition(position);
    }
    public void clearCardMatrix() {
        cardMatrix.clear();
    }

    public void clearFreeCorners(){
        freeCorners.clear();
    }
    public void addFreeCorners(List<ViewCorner> cardFreeCorners) {
        freeCorners.addAll(cardFreeCorners);

        if(board.getPlayerHand().getNickname().equals(owner))
            board.notifyView(SceneID.getMyAreaSceneID(),
                    new DisplayFreeCorners(owner, true, freeCorners));
        else
            board.notifyView(SceneID.getOpponentAreaSceneID(owner),
                    new DisplayFreeCorners(owner, false, freeCorners));
    }
}
