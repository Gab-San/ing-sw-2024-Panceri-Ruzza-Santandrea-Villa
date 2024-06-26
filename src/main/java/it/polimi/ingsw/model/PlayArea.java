package it.polimi.ingsw.model;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.GameSubject;
import it.polimi.ingsw.model.listener.remote.errors.IllegalActionError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalStateError;
import it.polimi.ingsw.model.listener.remote.events.playarea.FreeCornersUpdate;
import it.polimi.ingsw.model.listener.remote.events.playarea.PlayAreaPlacedCardEvent;
import it.polimi.ingsw.model.listener.remote.events.playarea.VisibleResourcesUpdateEvent;
import it.polimi.ingsw.network.CentralServer;
import org.jetbrains.annotations.NotNull;
import it.polimi.ingsw.GameResource;


import java.util.*;

/**
 * This class represents the play-area of a player: the space within which the cards are placed.
 * In this representation it is comprehensive of the visible resources and a list containing, all
 * the free corners on which placement is possible.
 */
public class PlayArea implements GameSubject {
    private final String owner;
    private final Map<GamePoint, PlaceableCard> cardMatrix;
    private final Map<GameResource, Integer> visibleResources;
    private final List<Corner> freeCorners;
    private final List<GameListener> gameListeners;
    /**
     * Default constructor.
     */
    public PlayArea(){
        this(null);
    }

    /**
     * Constructs a play area associated with the specified player.
     * @param owner player associated with the play area
     */
    public PlayArea(String owner){
        this.owner = owner;
        gameListeners = new LinkedList<>();
        cardMatrix = new Hashtable<>();
        visibleResources = new Hashtable<>();
        freeCorners = new LinkedList<>();
    }

    /**
     * Returns the map representation of the grid formed by the placed cards.
     * @return map representation of the grid formed by the placed cards
     */
    public Map<GamePoint, PlaceableCard> getCardMatrix(){
        return Collections.unmodifiableMap(cardMatrix);
    }

    /**
     * Returns map containing information of the amount of resources that are visible.
     * @return visible resources map
     */
    public Map<GameResource, Integer> getVisibleResources() {
        return Collections.unmodifiableMap(visibleResources);
    }

    /**
     * Returns a list of corners where a card can be placed.
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
        if(!cardMatrix.keySet().isEmpty()) {
            notifyAllListeners(new IllegalStateError(owner, "Attempting to place starting card on non-empty cardMatrix"));
            throw new IllegalStateException("Attempting to place starting card on non-empty cardMatrix");
        }
        // place card
        GamePoint cardPos = new GamePoint(0,0);
        PlaceableCard card = startCard.setPosition(cardPos);
        cardMatrix.put(cardPos, card);
        notifyAllListeners(new PlayAreaPlacedCardEvent(owner, card, cardPos));
        // add resources to visibleResources
        Map<GameResource, Integer> cardResources = card.getCardResources();
        for(GameResource r : cardResources.keySet()){
            visibleResources.put(r, cardResources.get(r)
                                    + (CentralServer.isResDebugMode() ? 100 : 0));
        }

        notifyAllListeners(new VisibleResourcesUpdateEvent(owner, visibleResources));
        // add corners to freeCorners
        freeCorners.addAll(card.getFreeCorners());
        notifyAllListeners(new FreeCornersUpdate(owner, freeCorners));
    }

    /**
     * Places a card in cardMatrix and occupies corners, updates visibleResources and freeCorners
     * @author Ruzza
     * @param playCard ResourceCard or GoldCard to be placed
     * @param corner which corner (in FreeCorner) to place it on
     * @throws IllegalStateException if the placement is invalid
     * @return placed card
     */
    public PlayCard placeCard(@NotNull PlayCard playCard, @NotNull Corner corner) throws IllegalStateException{
        // checks valid cost before placing
        Map<GameResource, Integer> placeCost = playCard.getPlacementCost();
        for (GameResource r : placeCost.keySet()){
            if (visibleResources.get(r) == null || visibleResources.get(r) < placeCost.get(r)){
                notifyAllListeners(new IllegalActionError(owner, "Card can't be placed as placement cost condition isn't satisfied".toUpperCase()));
                throw new IllegalStateException("Card can't be placed as placement cost condition isn't satisfied");
            }
        }

        GamePoint cardPos = corner.getCardRef().getPosition().move(corner.getDirection());

        //checks valid placement, throw IllegalStateException on failure
        if(cardMatrix.get(cardPos) != null){
            notifyAllListeners(new IllegalActionError(owner,"Placing on existing Card".toUpperCase()));
            throw new IllegalStateException("Placing on existing Card");
        }

        for (CornerDirection dir : CornerDirection.values()){
            PlaceableCard dirCard = cardMatrix.get(cardPos.move(dir));
            if(dirCard != null) {
                Corner dirCorner = dirCard.getCorner(dir.opposite());
                if (dirCorner.isOccupied()) {
                    notifyAllListeners(new IllegalActionError(owner, ("Should not place here, corner " + dir + " is filled").toUpperCase()));
                    throw new IllegalStateException("Should not place here, corner " + dir + " is filled");
                }
            }
        }

        // place card
        PlaceableCard card = playCard.setPosition(cardPos);
        cardMatrix.put(cardPos, card);
        notifyAllListeners(new PlayAreaPlacedCardEvent(owner, card, cardPos));

        // displayMessage adjacent cards,
        // subtract covered corners' resources from visibleResources,
        // displayMessage freeCorners (remove covered corners or add placed card's corner)
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
                    GamePoint pointToCheck = cardPos.move(dir);
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
                    GamePoint pointLocked = cardPos.move(dir);
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
        notifyAllListeners(new VisibleResourcesUpdateEvent(owner, visibleResources));
        notifyAllListeners(new FreeCornersUpdate(owner, freeCorners));
        return (PlayCard) card;
    }

    @Override
    public void addListener(GameListener listener) {
        synchronized (gameListeners) {
            gameListeners.add(listener);
        }
    }

    @Override
    public void removeListener(GameListener listener) {
        synchronized (gameListeners) {
            gameListeners.remove(listener);
        }
    }

    @Override
    public void notifyAllListeners(GameEvent event) throws ListenException {
        synchronized (gameListeners) {
            for (GameListener listener : gameListeners) {
                listener.listen(event);
            }
        }
    }
}
