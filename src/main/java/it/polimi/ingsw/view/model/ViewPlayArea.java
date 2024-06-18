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
     * Removes the corner from the freeCorners list
     * and handles GUI border/mouseListener removal
     * @param corner the corner to remove from freeCorners
     */
    private void removeFreeCorner(ViewCorner corner){
        corner.resetCorner();
        freeCorners.remove(corner);
    }

    /**
     * Places a card at position, covering corners and handling freeCorners list <br>
     * This function trusts the server and does not check the validity of the placement
     * @param position point on which to place the card
     * @param card card to place
     */
    public void placeCard(GamePoint position, ViewPlaceableCard card){
        setCard(position, card);
        List<ViewCorner> placementFreeCorners = new LinkedList<>();
        for(CornerDirection dir : CornerDirection.values()){
            ViewPlaceableCard dirCard = cardMatrix.get(position.move(dir));
            if(dirCard != null){
                dirCard.getCorner(dir.opposite()).cover();
                removeFreeCorner(dirCard.getCorner(dir.opposite()));
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
                    if(isCornerFree) placementFreeCorners.add(card.getCorner(dir));
                }
                else{    // if that corner is FILLED, then check if it's blocking placement on another freeCorner
                    GamePoint pointLocked = position.move(dir);
                    for(CornerDirection dir2 : CornerDirection.values()){
                        ViewPlaceableCard cardDir = cardMatrix.get(pointLocked.move(dir2));
                        if(cardDir != null) {
                            ViewCorner possibleLockedCorner = cardDir.getCorner(dir2.opposite());
                            if (possibleLockedCorner.getResource() != FILLED)
                                removeFreeCorner(possibleLockedCorner);
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

        addFreeCorners(placementFreeCorners);
    }
    /**
     * Places a card at position bypassing all checks.
     * @param position position to place the card on
     * @param card card to place
     */
    public synchronized void setCard(GamePoint position, ViewPlaceableCard card) {
        //this method is synchronized to prevent conflicts when reading/writing maxZ
        cardMatrix.put(position, card);
        card.setPosition(position);
        zLayerMatrix.put(position, maxZ++);
        card.setLayer(zLayerMatrix.get(position));
        firePropertyChange(ChangeNotifications.PLACED_CARD, null, card);
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
        //do not run resetCorner on each to prevent a bug that disables
        // the starting card corners when transitioning
        // from setup state to play state
//        freeCorners.forEach(
//                ViewCorner::resetCorner
//        );
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

//        firePropertyChange(ChangeNotifications.FREE_CORN_CHANGE, null, cardFreeCorners);

        cardFreeCorners.forEach(
                ViewCorner::activateCorner
        );
    }

    public String getOwner(){
        return owner;
    }


    //TODO: move this up
    private final Set<CornerDirection> dirs = Arrays.stream(CornerDirection.values()).collect(Collectors.toSet());

    /**
     * Updates the zLayerMatrix with valid values for each card. <br>
     * All adjacent z values will be different. z1 > z2 means that card1 covers card2.
     * @throws IllegalArgumentException if the assignment of z values can't be completed.
     */
    public void calculateZLayers() throws IllegalArgumentException{
        zLayerMatrix.clear();
        for (GamePoint pos : cardMatrix.keySet()) {
            //TODO: fix this to scale on size of cardMatrix
            zLayerMatrix.put(pos, 3000);
            cardMatrix.get(pos).setLayer(zLayerMatrix.get(pos));
        }

        // I will use big gaps (~1000) to prevent a case like these coverages: 0 -> x -> 1
        // where x -> y  means "x covers y" with x,y integer zLayer values
        // in this case (0 < x < 1) there are would be no integer solutions for x.

        // iteratively look at all cards and
        // adjust the layers of adjacent cards with equal zLayer
        Set<GamePoint> nonFinalizedPos = new HashSet<>(cardMatrix.keySet());
        Set<GamePoint> finalizedPos = new HashSet<>();
        final int MAX_ITERATIONS = 5000;
        int i=0;
        while(!nonFinalizedPos.isEmpty() &&  i < MAX_ITERATIONS){
            for (GamePoint pos : nonFinalizedPos) {
                int z = zLayerMatrix.get(pos);
                try{
                    int zChange = determineZChange(pos);
                    zLayerMatrix.put(pos, z + zChange);
                    cardMatrix.get(pos).setLayer(zLayerMatrix.get(pos));
                    firePropertyChange(ChangeNotifications.CARD_LAYER_CHANGE, null, cardMatrix.get(pos));
                    finalizedPos.add(pos);
                }catch (IllegalStateException ignored){} //due to being currently undecidable
            }
            if(finalizedPos.isEmpty())
                throw new IllegalArgumentException("UNDECIDABLE zLAYERING PROBLEM due to non-assignability");
            nonFinalizedPos.removeAll(finalizedPos);
            finalizedPos.clear();
            ++i;
        }
        if(i >= MAX_ITERATIONS)
            throw new IllegalArgumentException("MAX ITERATIONS REACHED");

        maxZ = zLayerMatrix.values().stream()
                .mapToInt(v->v).max().orElse(0);
    }
    /**
     * Return the zChange that fixes a position pos
     * @param pos the position where the zLayer should be checked/modified
     * @throws IllegalStateException if the z values is currently undetermined, but it may be found in future iterations.
     * @throws IllegalArgumentException if the assignment of z values can't be completed.
     */
    private int determineZChange(GamePoint pos)throws IllegalStateException, IllegalArgumentException{
        ViewPlaceableCard card = cardMatrix.get(pos);
        int zCard = zLayerMatrix.get(pos);
        int zChange;
        //TODO: fix this to scale on size of cardMatrix
        final int UNBOUND_BIG_CHANGE = 5000;

        List<Boolean> covers = dirs.stream()
                .filter(d -> zLayerMatrix.get(pos.move(d)) != null)
                .filter(d -> zLayerMatrix.get(pos.move(d)) == zCard) //only dirs with equal zValue
                .map(d -> card.getCorner(d).isVisible())
                .distinct().toList();
        //covers contains true/false for each adjacent card *with the same z value*

        if(covers.size() > 1) { //in this case card is both covering and covered by a card with the same zLayer value
            throw new IllegalStateException();
        }else{
            if(covers.isEmpty()){
                zChange = 0; //no issues if all zLayers are different and valid for coverage
                if(!validateZ(pos, zChange)){
                    zChange = 1000*discernInvalidation(pos, zChange);
                }
            }
            else {
                //here I know that:
                // -    at least one adjacent card has the same zLayer value
                // -    all such cards either cover or are covered by this card
                //      (thus covers contains one boolean, true if such cards are covered by this card)
                boolean isCoveringOthers = covers.get(0);
                List<Integer> diffs = dirs.stream()
                        .map(d -> zLayerMatrix.get(pos.move(d)))
                        .filter(Objects::nonNull)
                        .map(i -> i - zCard) //operate on differences
                        //if this card is covering,
                        // then this card's zLayer must be increased
                        // then I only look at diffs>0 --> otherZ > zCard
                        .filter(i -> isCoveringOthers ? i > 0 : i < 0)
                        .sorted(Integer::compare) //sort ascending
                        .toList();
                if (diffs.isEmpty()) //unbounded increase/decrease
                    // use a big number for the same problem described above
                    zChange = isCoveringOthers ? UNBOUND_BIG_CHANGE : -UNBOUND_BIG_CHANGE;
                else {
                    //diffs contains all relevant differences in zLayer
                    // (only >0 is this covers them) (only <0 if they cover this)
                    int minDiff = diffs.get(0);
                    if(Math.abs(minDiff) > 128)
                        zChange = (int) (Math.signum(minDiff) * (Math.abs(minDiff) - 50));
                    else
                        zChange = minDiff / 2;
                    if (zChange == 0)
                        throw new IllegalArgumentException("UNDECIDABLE zLAYERING PROBLEM: found a diff of " + minDiff);
                }
            }
        }

        final int MAX_HALVING = 20;
        for (int i = 0; i < MAX_HALVING; i++) {
            if(validateZ(pos,zChange))
                return zChange;
            else
                zChange /= 2;
            if(zChange == 0){
                System.out.println("zChange=0 at #iter="+i);
                break;
            }
        }
        throw new IllegalArgumentException("INVALID CHANGE AT " + pos + " #iter=OVER-MAX" + " zChange="+zChange);
        //TODO: fix invalid zChange?? [Ale] more testing to validate this method
    }

    /**
     * @param pos position to validate
     * @param zChange the proposed change in z value
     * @return true if the change leads to a valid state of zLayers <br>
     *        (no duplicates, covers all z'< z and is covered by all z''>z)
     */
    private boolean validateZ(GamePoint pos, int zChange) {
        return discernInvalidation(pos, zChange) == 100;
    }

    /**
     * @param pos position to validate
     * @param zChange the proposed change in z value
     * @return 0 if there is a direction with equal z <br>
     *         1 if there is a direction with z' > z and card covers card' <br>
     *        -1 if there is a direction with z' < z and card' covers card
     */
    private int discernInvalidation(GamePoint pos, int zChange){
        for (CornerDirection dir : dirs) {
            int newZ = zLayerMatrix.get(pos) + zChange;
            Integer dirZ = zLayerMatrix.get(pos.move(dir));
            boolean coversDir = cardMatrix.get(pos).getCorner(dir).isVisible();
            if(dirZ != null){
                if(dirZ == newZ) return 0;
                if(dirZ > newZ && coversDir) return 1;
                if(dirZ < newZ && !coversDir) return -1;
            }
        }
        return 100;
    }


    /*
    final int MAX_HALVING = zChange/25; //2^5 = 32
        for (int i = 0; i < MAX_HALVING; i++) {
            if(validateZ(pos,zChange))
                return zChange;
            else{
                if(Math.abs(zChange) > 128)
                    zChange = (int) (Math.signum(zChange) * (Math.abs(zChange) - 50));
                else
                    zChange = zChange / 2;
            }
            System.out.println("INVALID CHANGE AT " + pos + " #iter="+i);
        }
     */
}
