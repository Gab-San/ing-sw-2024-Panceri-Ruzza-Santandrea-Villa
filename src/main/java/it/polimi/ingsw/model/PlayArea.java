package it.polimi.ingsw.model;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.GameSubject;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.exceptions.ListenException;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayArea implements GameSubject {
    private final Map<Point, PlaceableCard> cardMatrix;
    private final Hashtable<GameResource, Integer> visibleResources;
    private final List<Corner> freeCorners;

    /**
     * Constructs PlayArea with empty cardMatrix, no visible resources and no freeCorners
     */
    public PlayArea(){
        cardMatrix = new Hashtable<>();
        visibleResources = new Hashtable<>();
        freeCorners = new LinkedList<>();
    }

    public Map<Point, PlaceableCard> getCardMatrix(){
        return Collections.unmodifiableMap(cardMatrix);
    }

    /**
     * @return map containing information of the amount of resources that are visible
     */
    public Map<GameResource, Integer> getVisibleResources() {
        return Collections.unmodifiableMap(visibleResources);
    }

    /**
     * @return list of corners where a card can be placed
     */
    public List<Corner> getFreeCorners() {
        return Collections.unmodifiableList(freeCorners);
    }

    /**
     * Places the startingCard in cardMatrix, updates visibleResources and freeCorners
     * @author Ruzza
     * @param startCard StartingCard to be placed
     * @throws IllegalStateException if the cardMatrix isn't empty
     */
    public void placeStartingCard(@NotNull StartingCard startCard) throws IllegalStateException{
        // check cardMatrix is empty
        if(!cardMatrix.keySet().isEmpty())
            throw new IllegalStateException("Attempting to place starting card on non-empty cardMatrix");

        // place card
        Point cardPos = new Point(0,0);
        PlaceableCard card = startCard.setPosition(cardPos);
        cardMatrix.put(cardPos, card);

        // add resources to visibleResources
        Map<GameResource, Integer> cardResources = card.getCardResources();
        for(GameResource r : cardResources.keySet()){
            visibleResources.put(r, cardResources.get(r));
        }
        // add corners to freeCorners
        freeCorners.addAll(card.getFreeCorners());
    }
    /**
     * Places a card in cardMatrix and occupies corners, updates visibleResources and freeCorners
     * @author Ruzza
     * @param playCard ResourceCard or GoldCard to be placed
     * @param corner which corner (in FreeCorner) to place it on
     * @throws IllegalStateException if the placement is invalid
     */
    public PlayCard placeCard(@NotNull PlayCard playCard, @NotNull Corner corner) throws IllegalStateException{
        // checks valid cost before placing
        Map<GameResource, Integer> placeCost = playCard.getPlacementCost();
        for (GameResource r : placeCost.keySet()){
            if (visibleResources.get(r) == null || visibleResources.get(r) < placeCost.get(r)){
                throw new IllegalStateException("Card can't be placed as placement cost condition isn't satisfied");
            }
        }
        Point cardPos = corner.getCardRef().getPosition().move(corner.getDirection());

        //checks valid placement, throw IllegalStateException on failure
        if(cardMatrix.get(cardPos) != null) throw new IllegalStateException("Placing on existing Card");
        for (CornerDirection dir : CornerDirection.values()){
            PlaceableCard dirCard = cardMatrix.get(cardPos.move(dir));
            if(dirCard != null) {
                Corner dirCorner = dirCard.getCorner(dir.opposite());
                if (dirCorner.isOccupied())
                    throw new IllegalStateException("Should not place here, corner " + dir + " is occupied");
            }
        }

        // place card
        PlaceableCard card = playCard.setPosition(cardPos);
        cardMatrix.put(cardPos, card);

        // update adjacent cards,
        // subtract covered corners' resources from visibleResources,
        // update freeCorners (remove covered corners or add placed card's corner)
        for (CornerDirection dir : CornerDirection.values()){
            PlaceableCard dirCard = cardMatrix.get(cardPos.move(dir));
            if(dirCard != null) {
                Corner dirCorner = dirCard.getCorner(dir.opposite()); // corner isn't filled as checks passed
                if (dirCorner.getResource() != null && dirCorner.getResource() != GameResource.FILLED){
                    visibleResources.put(
                            dirCorner.getResource(),
                            visibleResources.get(dirCorner.getResource()) - 1
                    ); // resource is always present in the visibleResource map as it was added on previous placement
                }
                dirCorner.cover().occupy();
                card.getCorner(dir).occupy();
                freeCorners.remove(dirCorner);
            }
            else{
                if(!card.getCorner(dir).isOccupied()) { // if that corner isn't FILLED
                    boolean isCornerFree = true;
                    Point pointToCheck = cardPos.move(dir);
                    for(CornerDirection dir2 : CornerDirection.values()) {
                        PlaceableCard cardDir = cardMatrix.get(pointToCheck.move(dir2));
                        if (cardDir != null && cardDir.getCorner(dir2.opposite()).isOccupied()) {
                            isCornerFree = false;
                            break;
                        }
                    }
                    if(isCornerFree) freeCorners.add(card.getCorner(dir));
                }
                else{    // if that corner is FILLED, then check if it's blocking placement on another freeCorner
                    Point pointLocked = cardPos.move(dir);
                    for(CornerDirection dir2 : CornerDirection.values()){
                        PlaceableCard cardDir = cardMatrix.get(pointLocked.move(dir2));
                        if(cardDir != null) {
                            Corner lockedCorner = cardDir.getCorner(dir2.opposite());
                            lockedCorner.occupy();
                            freeCorners.remove(lockedCorner);
                        }
                    }
                }
            }
        }

        // add placed card resources to visibleResources
        Map<GameResource, Integer> cardResources = card.getCardResources();
        for (GameResource res : cardResources.keySet()){
            int currentVal = (visibleResources.get(res) == null) ? 0 : visibleResources.get(res);
            visibleResources.put(
                    res,
                    currentVal + cardResources.get(res)
            );

        }
        return (PlayCard) card;
    }

    @Override
    public void addListener(GameListener listener) {

    }

    @Override
    public void removeListener(GameListener listener) {

    }

    @Override
    public void notifyAllListeners(GameEvent event) {

    }

    @Override
    public void notifyListener(GameListener listener, GameEvent event) throws ListenException {

    }
}
