package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.model.enums.GameResource.*;
import static it.polimi.ingsw.model.enums.CornerDirection.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayAreaTest {
    PlayArea playArea;
    StartingCard startingCard;
    Map<GameResource, Integer> oldVisibleRes;
    List<Corner> oldFreeCorners;

    void setUp(boolean placeStartingCardOnFront) {
        playArea = new PlayArea();
        Hashtable<CornerDirection, GameResource> frontRes = new Hashtable<>();
        frontRes.put(TL, LEAF);
        frontRes.put(BL, MUSHROOM);
        startingCard = new StartingCard(
                frontRes,
                new GameResource[]{LEAF, WOLF, MUSHROOM},
                new Corner(LEAF, TL),
                new Corner(WOLF, TR),
                new Corner(MUSHROOM, BL),
                new Corner(BUTTERFLY, BR)
        );

        if (!placeStartingCardOnFront)
            startingCard.flip();

        playArea.placeStartingCard(startingCard);

        oldVisibleRes = new Hashtable<>();
        oldVisibleRes.putAll(playArea.getVisibleResources());

        oldFreeCorners = new ArrayList<>();
        oldFreeCorners.addAll(playArea.getFreeCorners());
    }

    @Test
    @DisplayName("Test placement of starting card in front (central resources visible)")
    void placeStartingCard_front() {
        setUp(true);
        //assert correct visibleResources added
        Map<GameResource, Integer> visibleRes = playArea.getVisibleResources();
        assertEquals(2, visibleRes.get(LEAF));
        assertEquals(1, visibleRes.get(WOLF));
        assertEquals(2, visibleRes.get(MUSHROOM));
        assertEquals(0, visibleRes.get(BUTTERFLY));
        for(GameResource res : GameResource.values()){
            if(res.getResourceIndex() > 4)
                assertNull(visibleRes.get(res));
        }

        //assert correct freeCorners added
        StartingCard startingCard = (StartingCard) playArea.getCardMatrix().get(new Point(0,0));
        assertEquals(startingCard, this.startingCard);
        assertTrue(playArea.getFreeCorners().containsAll(startingCard.getFreeCorners()));

        //assert exception on placement of another starting card
        assertThrows(RuntimeException.class, ()->playArea.placeStartingCard(startingCard));
    }
    @Test
    @DisplayName("Test placement of starting card on back (central resources not visible)")
    void placeStartingCard_back() {
        setUp(false);
        //assert correct visibleResources added
        Map<GameResource, Integer> visibleRes = playArea.getVisibleResources();
        assertEquals(1, visibleRes.get(LEAF));
        assertEquals(1, visibleRes.get(WOLF));
        assertEquals(1, visibleRes.get(MUSHROOM));
        assertEquals(1, visibleRes.get(BUTTERFLY));
        for(GameResource res : GameResource.values()){
            if(res.getResourceIndex() > 4)
                assertNull(visibleRes.get(res));
        }

        //assert correct freeCorners added
        StartingCard startingCard = (StartingCard) playArea.getCardMatrix().get(new Point(0,0));
        assertEquals(startingCard, this.startingCard);
        assertTrue(playArea.getFreeCorners().containsAll(startingCard.getFreeCorners()));

        //assert exception on placement of another starting card
        assertThrows(RuntimeException.class, ()->playArea.placeStartingCard(startingCard));
    }

    @Test
    @DisplayName("Test placement of a PlayCard")
    void placeCard() {
        setUp(false);

        PlayCard cardToPlace = new ResourceCard(LEAF, 1,
                new Corner(null, TL),
                new Corner(MUSHROOM, TR),
                new Corner(LEAF, BL)
                // BR is filled
        );
        Corner coveredCorner = playArea.getFreeCorners().get(0);
        playArea.placeCard(cardToPlace, coveredCorner);

        Point placePosition = coveredCorner.getCardRef().getPosition().move(coveredCorner.getDirection());
        PlaceableCard cardPlaced = playArea.getCardMatrix().get(placePosition);

        // assert correct placement
        assertEquals(cardPlaced.getPosition(), placePosition);
        assertEquals(cardPlaced.getCardResources(), cardToPlace.getCardResources());
        assertEquals(cardPlaced.getCardColour(), cardToPlace.getCardColour());

        // assert coveredCorner actually occupied and covered
        assertTrue(coveredCorner.isOccupied());
        assertFalse(coveredCorner.isVisible());

        // assert placed card's corner occupied and visible
        Corner placedCard_corner = cardPlaced.getCorner(coveredCorner.getDirection().opposite());
        assertTrue(placedCard_corner.isOccupied());
        assertTrue(placedCard_corner.isVisible());

        // assert visibleResources update
        oldVisibleRes.replace(
                coveredCorner.getResource(),
                oldVisibleRes.get(coveredCorner.getResource())-1
        );
        Map<GameResource, Integer> placedCardRes = cardPlaced.getCardResources();
        for(GameResource res : placedCardRes.keySet()){
            oldVisibleRes.replace(
                res,
                oldVisibleRes.get(res) + placedCardRes.get(res)
            );
        }
        assertEquals(oldVisibleRes, playArea.getVisibleResources());

        // assert freeCorners update
        assertFalse(playArea.getFreeCorners().contains(coveredCorner));
        oldFreeCorners.remove(coveredCorner);
        assertTrue(playArea.getFreeCorners().containsAll(oldFreeCorners));
        assertTrue(playArea.getFreeCorners().containsAll(cardPlaced.getFreeCorners()));
        assertFalse(playArea.getFreeCorners().contains(placedCard_corner));
        // TODO: review placeCard test
    }
}