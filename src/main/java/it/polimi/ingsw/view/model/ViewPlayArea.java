package it.polimi.ingsw.view.model;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.events.update.DisplayFreeCorners;
import it.polimi.ingsw.view.events.update.DisplayPlaceCard;
import it.polimi.ingsw.view.events.update.DisplayVisibleResources;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.GameResource.FILLED;

/**
 * Represents any player's PlayArea (as the place where their cards are placed)
 */
public class ViewPlayArea extends JComponent {
    /**
     * This playArea owner's nickname
     */
    private final String owner;
    private final Map<GamePoint, ViewPlaceableCard> cardMatrix;
    private final Map<GameResource, Integer> visibleResources;
    private final List<ViewCorner> freeCorners;
    private final ViewBoard board;
    //zLayer information is needed for GUI image layering
    private final Map<GamePoint, Integer> zLayerMatrix;
    private int maxZ;

    /**
     * Constructs the playArea.
     * @param owner this playArea owner's nickname.
     * @param board the board this playArea belongs to.
     */
    public ViewPlayArea(String owner, ViewBoard board) {
        this.owner = owner;
        this.board = board;
        this.cardMatrix = Collections.synchronizedMap(new Hashtable<>());
        this.visibleResources = Collections.synchronizedMap(new Hashtable<>());
        this.freeCorners = Collections.synchronizedList(new LinkedList<>());
        this.zLayerMatrix = Collections.synchronizedMap(new Hashtable<>());
        maxZ = 0;
    }
    /**
     * Places a card at position (0,0), handling freeCorners list <br>
     * @param startCard starting card to place
     */
    public void placeStarting(ViewStartCard startCard){
        GamePoint zero = new GamePoint(0,0);
        setCard(zero, startCard);
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

    /**
     * Validates the placement by checking the satisfaction of the game rules:
     * <ul>
     *     <li>
     *         Visible resources satisfy the placement cost.
     *     </li>
     *     <li>
     *         All corners covered are not filled and belong to different cards.
     *     </li>
     * </ul>
     * @param position position where the card would be placed
     * @param playCard card that would be placed
     * @return true if the proposed placement is valid (does *not* perform the placement)
     */
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

    /**
     * @param cardID the ID of the card to find in this playArea
     * @return the position of the requested card.
     * @throws IllegalArgumentException if there is no card with given ID in this playArea
     */
    public GamePoint getPositionByID(String cardID) throws IllegalArgumentException{
        synchronized (cardMatrix){
            return cardMatrix.values().stream()
                    .filter(c->c.getCardID().equals(cardID))
                    .findFirst()
                    .orElseThrow(()->new IllegalArgumentException("Your playArea does not contain card "+ cardID))
                    .getPosition();
        }
    }

    /**
     * @param position position to read
     * @return the card that was placed at given position (or null if there is no card there)
     */
    public ViewPlaceableCard getCardAt(GamePoint position){
        return cardMatrix.get(position);
    }
    /**
     * @param row y coordinate of the position
     * @param col x coordinate of the position
     * @return the card that was placed at given position (or null if there is no card there)
     */
    public ViewPlaceableCard getCardAt(int row, int col){
        return getCardAt(new GamePoint(row, col));
    }

    /**
     * @return an unmodifiable, synchronized view of this playArea's card matrix.
     */
    public Map<GamePoint, ViewPlaceableCard> getCardMatrix(){ return Collections.unmodifiableMap(cardMatrix);}

    /**
     * @return an unmodifiable, synchronized view of this playArea's zLayer matrix (for the GUI).
     */
    public Map<GamePoint, Integer> getZLayerMatrix(){ return Collections.unmodifiableMap(zLayerMatrix);}

    /**
     * Sets the visible resources of this playArea (overriding the old values). <br>
     * Note that the map is copied by value, not by reference. <br>
     * Also notifies the owner's scene of the visibleResources update event.
     * @param visibleResources the new map of visible resources
     */
    public void setVisibleResources(Map<GameResource, Integer> visibleResources){
        this.visibleResources.clear();
        this.visibleResources.putAll(visibleResources);
        if(board.getPlayerHand().getNickname().equals(owner))
            board.notifyView(SceneID.getMyAreaSceneID(),
                    new DisplayVisibleResources(owner, true, visibleResources));
        else
            board.notifyView(SceneID.getOpponentAreaSceneID(owner),
                    new DisplayVisibleResources(owner, false, visibleResources));

        firePropertyChange(ChangeNotifications.VIS_RES_CHANGE, null, visibleResources);
    }
    /**
     * @return an unmodifiable, synchronized view of this playArea's visible resources map.
     */
    public Map<GameResource, Integer> getVisibleResources(){
        return Collections.unmodifiableMap(visibleResources);
    }
    /**
     * @return an unmodifiable, synchronized view of this playArea's free corners list.
     */
    public List<ViewCorner> getFreeCorners(){
        return Collections.unmodifiableList(freeCorners);
    }

    /**
     * Places a card at position bypassing all checks.
     * @param position position to place the card on
     * @param card card to place
     */
    public synchronized void setCard(GamePoint position, ViewPlaceableCard card) {
        cardMatrix.put(position, card);
        card.setPosition(position);
        zLayerMatrix.put(position, maxZ++);
        //this method is synchronized to prevent conflicts when reading/writing maxZ
    }

    /**
     * Resets this playArea's information on placed cards.
     */
    public void clearCardMatrix() {
        cardMatrix.clear();
        zLayerMatrix.clear();
    }
    /**
     * Resets this playArea's free corners list.
     */
    public void clearFreeCorners(){
        freeCorners.clear();
    }

    /**
     * Adds a list of free corners to the freeCorners list. <br>
     * Also notifies the playArea owner's scene of the addFreeCorners event
     * @param cardFreeCorners the new freeCorners to add to the list.
     */
    public void addFreeCorners(List<ViewCorner> cardFreeCorners) {
        freeCorners.addAll(cardFreeCorners);

        if(board.getPlayerHand().getNickname().equals(owner))
            board.notifyView(SceneID.getMyAreaSceneID(),
                    new DisplayFreeCorners(owner, true, freeCorners));
        else
            board.notifyView(SceneID.getOpponentAreaSceneID(owner),
                    new DisplayFreeCorners(owner, false, freeCorners));

        firePropertyChange(ChangeNotifications.FREE_CORN_CHANGE, null, cardFreeCorners);
    }

    public String getOwner(){
        return owner;
    }
}
