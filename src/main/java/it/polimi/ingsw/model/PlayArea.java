package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayArea {
    private final Map<Point, PlaceableCard> cardMatrix;
    private final Hashtable<GameResource, Integer> visibleResources;
    private final List<Corner> freeCorners;

    public PlayArea(){
        cardMatrix = new Hashtable<>();
        visibleResources = new Hashtable<>();
        freeCorners = new ArrayList<>(10);
    }

    public Map<Point, PlaceableCard> getCardMatrix(){
        return Collections.unmodifiableMap(cardMatrix);
    }

    public Map<GameResource, Integer> getVisibleResources() {
        return Collections.unmodifiableMap(visibleResources);
    }
    public List<Corner> getFreeCorners() {
        return Collections.unmodifiableList(freeCorners);
    }

    /**
     * Places the startingCard in cardMatrix, updates visibleResources and freeCorners
     * @author Ruzza
     * @param card StartingCard to be placed
     * @throws RuntimeException if the cardMatrix isn't empty
     */
    public void placeStartingCard(@NotNull StartingCard card) throws RuntimeException{
        // check cardMatrix is empty
        if(!cardMatrix.keySet().isEmpty())
            throw new RuntimeException("Attempting to place starting card on non-empty cardMatrix");

        // add resources to visibleResources
        Map<GameResource, Integer> cardResources = card.getCardResources();
        for(GameResource r : cardResources.keySet()){
            visibleResources.put(r, cardResources.get(r));
        }
        // add corners to freeCorners
        freeCorners.addAll(card.getFreeCorners());
        // place card
        Point cardPos = new Point(0,0);
        cardMatrix.put(cardPos, card);
        card.setPosition(cardPos);
    }
    /**
     * Places a card in cardMatrix and occupies corners, updates visibleResources and freeCorners
     * @author Ruzza
     * @param card ResourceCard or GoldCard to be placed
     * @param corner which corner (in FreeCorner) to place it on
     * @throws RuntimeException if the placement is invalid
     */
    public void placeCard(@NotNull PlayCard card, @NotNull Corner corner) throws RuntimeException{
        // checks valid cost before placing
        Map<GameResource, Integer> placeCost = card.getPlacementCost();
        for (GameResource r : placeCost.keySet()){
            if (visibleResources.get(r) == null || visibleResources.get(r) < placeCost.get(r)){
                throw new RuntimeException("Card can't be placed as placement cost condition isn't satisfied");
            }
        }

        Point cardPos = corner.getCardRef().getPosition().move(corner.getDirection());
        //checks valid placement, throw RuntimeException on failure
        if(cardMatrix.get(cardPos) != null) throw new RuntimeException("Placing on existing Card");
        for (CornerDirection dir : CornerDirection.values()){
            PlaceableCard dirCard = cardMatrix.get(cardPos.move(dir));
            if(dirCard != null) {
                Corner dirCorner = dirCard.getCorner(dir.opposite()); // throws RuntimeException if it's filled
                if (dirCorner.isOccupied())
                    throw new RuntimeException("Should not place here, corner " + dir + " is occupied");
            }
        }
        // update adjacent cards,
        // subtract covered corners' resources from visibleResources,
        // update freeCorners (remove covered corners or add placed card's corner)
        for (CornerDirection dir : CornerDirection.values()){
            PlaceableCard dirCard = cardMatrix.get(cardPos.move(dir));
            if(dirCard != null) {
                Corner dirCorner = dirCard.getCorner(dir.opposite()); // corner isn't filled as checks passed
                if (dirCorner.getResource() != null){
                    visibleResources.put(
                            dirCorner.getResource(),
                            visibleResources.get(dirCorner.getResource()) - 1
                    ); // resource is always present in the visibleResource map as it was added on previous placement
                }
                dirCorner.cover().occupy();
                try{
                    card.getCorner(dir).occupy();
                }catch (Exception ignored){}
                freeCorners.remove(dirCorner);
            }
            else{
                try {
                    freeCorners.add(card.getCorner(dir));
                }catch (Exception ignored){} // filled corners aren't free
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
        // place card
        cardMatrix.put(cardPos, card);
        card.setPosition(cardPos);
    }
}
