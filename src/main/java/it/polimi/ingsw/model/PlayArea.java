package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.enums.Resource;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayArea {
    private final DoubleMap<PlaceableCard> cardMatrix;
    private final Map<Resource, Integer> visibleResources;
    private final List<Corner> freeCorners;

    public PlayArea(){
        cardMatrix = new DoubleMap<>();
        visibleResources = new HashMap<>();
        freeCorners = new ArrayList<>(10);
    }

    public DoubleMapRO<PlaceableCard> getCardMatrix(){
        return cardMatrix;
    }

    public Map<Resource, Integer> getVisibleResources() {
        return Collections.unmodifiableMap(visibleResources);
    }
    public List<Corner> getFreeCorners() {
        return Collections.unmodifiableList(freeCorners);
    }

    /**
     * Places a card in cardMatrix and occupies corners, updates resources and freeCorner
     * @author Ruzza
     * @param card ResourceCard or GoldCard to be placed
     * @param corner which corner (in FreeCorner) to place it on
     */
    public void placeCard(@NotNull PlayCard card, @NotNull Corner corner){
        if (corner.isOccupied()){
            throw new Error("Should not place here");
        }
        int row = corner.getCardRef().getRow();
        int col = corner.getCardRef().getCol();
        col = cardMatrix.moveCol(row, col, corner.getDirection());
        row = cardMatrix.moveRow(row, col, corner.getDirection());

        // calculate point where card will be placed using corner direction


        corner.occupy();
        corner.cover();

    }
}
