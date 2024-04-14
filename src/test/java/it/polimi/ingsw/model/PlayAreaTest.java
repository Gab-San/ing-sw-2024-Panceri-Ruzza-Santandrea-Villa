package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.cardstrategies.CornerCoverGoldCard;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        // TODO: fix corners with front and back resources
        startingCard = new StartingCard(
                new GameResource[]{LEAF, WOLF, MUSHROOM},
                new Corner(null, LEAF, TL),
                new Corner(null, WOLF, TR),
                new Corner(FILLED, MUSHROOM, BL),
                new Corner(FILLED, BUTTERFLY, BR)
        );

        if (placeStartingCardOnFront)
            startingCard.turnFaceUp();
        else
            startingCard.turnFaceDown();

        playArea.placeStartingCard(startingCard);

        oldVisibleRes = new Hashtable<>();
        oldVisibleRes.putAll(playArea.getVisibleResources());

        oldFreeCorners = new ArrayList<>();
        oldFreeCorners.addAll(playArea.getFreeCorners());
    }

    @Test
    @DisplayName("Test placement of starting card on front (central resources visible)")
    void testPlaceStartingCard_front() {
        setUp(true);
        //assert correct visibleResources added
        Map<GameResource, Integer> visibleRes = playArea.getVisibleResources();
        assertEquals(1, visibleRes.get(LEAF));
        assertEquals(1, visibleRes.get(WOLF));
        assertEquals(1, visibleRes.get(MUSHROOM));
        assertEquals(0, visibleRes.get(BUTTERFLY));
        for(GameResource res : GameResource.values()){
            if(res.getResourceIndex() > 4)
                assertEquals(0, visibleRes.get(res));
        }

        //assert correct freeCorners added
        StartingCard startingCard = (StartingCard) playArea.getCardMatrix().get(new Point(0,0));
        assertEquals(startingCard, this.startingCard);
        assertEquals(playArea.getFreeCorners(), startingCard.getFreeCorners());

        //assert exception on placement of another starting card
        assertThrows(RuntimeException.class, ()->playArea.placeStartingCard(startingCard));
    }
    @Test
    @DisplayName("Test placement of starting card on back (central resources not visible)")
    void testPlaceStartingCard_back() {
        setUp(false);
        //assert correct visibleResources added
        Map<GameResource, Integer> visibleRes = playArea.getVisibleResources();
        assertEquals(1, visibleRes.get(LEAF));
        assertEquals(1, visibleRes.get(WOLF));
        assertEquals(1, visibleRes.get(MUSHROOM));
        assertEquals(1, visibleRes.get(BUTTERFLY));
        for(GameResource res : GameResource.values()){
            if(res.getResourceIndex() > 4)
                assertEquals(0, visibleRes.get(res));
        }

        //assert correct freeCorners added
        StartingCard startingCard = (StartingCard) playArea.getCardMatrix().get(new Point(0,0));
        assertEquals(startingCard, this.startingCard);
        assertEquals(playArea.getFreeCorners(), startingCard.getFreeCorners());

        //assert exception on placement of another starting card
        assertThrows(RuntimeException.class, ()->playArea.placeStartingCard(startingCard));
    }

    @Test
    @DisplayName("Test placement of a PlayCard")
    void testPlaceCard() {
        setUp(false);

        PlayCard cardToPlace = new ResourceCard(LEAF, 1,
                new Corner(null, TL),
                new Corner(MUSHROOM, TR),
                new Corner(LEAF, BL)
                // BR is filled
        );
        Corner coveredCorner = playArea.getFreeCorners().get(0);
        // remove that corner's resource from oldVisibleResources before it is covered
        if(coveredCorner.getResource() != null)
            oldVisibleRes.replace(coveredCorner.getResource(), oldVisibleRes.get(coveredCorner.getResource())-1);
        // place card, assert no exception raised
        assertDoesNotThrow(() -> playArea.placeCard(cardToPlace, coveredCorner));

        Point placePosition = coveredCorner.getCardRef().getPosition().move(coveredCorner.getDirection());
        PlaceableCard cardPlaced = playArea.getCardMatrix().get(placePosition);

        // assert correct placement
        assertEquals(cardPlaced.getPosition(), placePosition);
        assertEquals(cardPlaced, cardToPlace);

        // assert coveredCorner actually occupied and covered
        assertTrue(coveredCorner.isOccupied());
        assertFalse(coveredCorner.isVisible());

        // assert placed card's corner occupied and visible
        Corner placedCard_corner = cardPlaced.getCorner(coveredCorner.getDirection().opposite());
        assertTrue(placedCard_corner.isOccupied());
        assertTrue(placedCard_corner.isVisible());

        // assert visibleResources update
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
        oldFreeCorners.addAll(cardPlaced.getFreeCorners());
        assertEquals(playArea.getFreeCorners(), oldFreeCorners);
        assertFalse(playArea.getFreeCorners().contains(placedCard_corner));
        // TODO: review placeCard test
    }

    private Map<Point, PlaceableCard> duplicateCardMatrix(){
        //duplicate playArea to check for invariance after failed placeCard attempts
        Map<Point, PlaceableCard> oldCardMatrix = new Hashtable<>();
        Map<Point, PlaceableCard> cardMatrix = playArea.getCardMatrix();
        for(Point p : cardMatrix.keySet()){
            oldCardMatrix.put(p, cardMatrix.get(p));
        }
        return oldCardMatrix;
    }
    @Test
    @DisplayName("Test invalid placement of a PlayCard (on FILLED corner)")
    void testPlaceCard_FILLED() {
        setUp(false);
        PlayCard blockedCard = new ResourceCard(MUSHROOM, 0,
                new Corner(FILLED, TL)
        ); // all front corners are filled
        blockedCard.turnFaceUp();
        assertTrue(blockedCard.getFreeCorners().isEmpty());

        Corner corner = playArea.getFreeCorners().get(2);
        playArea.placeCard(blockedCard, corner);

        //duplicate playArea to check for invariance after failed placeCard attempts
        Map<Point, PlaceableCard> oldCardMatrix = new Hashtable<>();
        Map<Point, PlaceableCard> cardMatrix = playArea.getCardMatrix();
        for(Point p : cardMatrix.keySet()){
            oldCardMatrix.put(p, cardMatrix.get(p));
        }

        PlayCard cardToPlace = new ResourceCard();
        assertThrows(RuntimeException.class,
                ()->playArea.placeCard(cardToPlace, blockedCard.getCorner(BL))
        );
        assertEquals(oldCardMatrix, cardMatrix);
        assertThrows(RuntimeException.class,
                ()->playArea.placeCard(cardToPlace, blockedCard.getCorner(TR))
        );
        assertEquals(oldCardMatrix, cardMatrix);
    }
    @Test
    @DisplayName("Test invalid placement of a PlayCard (on occupied corner)")
    void testPlaceCard_occupied() {
        setUp(false);
        PlayCard card = new ResourceCard(MUSHROOM, 0,
                new Corner(LEAF, TL),
                new Corner(QUILL, TR),
                new Corner(BUTTERFLY, BL),
                new Corner(MUSHROOM, BR)
        );
        card.turnFaceUp();

        Corner startCardCorner = playArea.getFreeCorners().get(0);
        playArea.placeCard(card, startCardCorner);

        //duplicate playArea to check for invariance after failed placeCard attempts
        Map<Point, PlaceableCard> oldCardMatrix = duplicateCardMatrix();

        PlayCard otherCard = new ResourceCard();
        // test of failure on occupied non-visible corner
        assertThrows(RuntimeException.class,
                ()->playArea.placeCard(otherCard, startCardCorner));
        assertEquals(oldCardMatrix, playArea.getCardMatrix());

        // test of failure on occupied visible corner
        assertThrows(RuntimeException.class,
                ()->playArea.placeCard(otherCard, card.getCorner(startCardCorner.getDirection().opposite())));
        assertEquals(oldCardMatrix, playArea.getCardMatrix());
    }

    private @NotNull ResourceCard createLEAFcard(){
        return new ResourceCard(LEAF, 1,
                new Corner(LEAF, TL),
                new Corner(LEAF, TR),
                new Corner(LEAF, BL),
                new Corner(LEAF, BR)
        );
    }
    @Test
    @DisplayName("Test placement of a GoldCard (placementCost satisfied)")
    void testPlaceGoldCard_OK() {
        setUp(false);

        Hashtable<GameResource, Integer> placementCost = new Hashtable<>();
        placementCost.put(LEAF, 5);

        // after setUp, visibleResources contains one of each
        // place cards to put 5 leaves in visibleResources
        ResourceCard card = createLEAFcard();
        card.flip();
        playArea.placeCard(card, playArea.getFreeCorners().get(0));
        card = createLEAFcard();
        card.flip();
        playArea.placeCard(card, playArea.getFreeCorners().get(0));
        card = createLEAFcard();
        card.flip();
        playArea.placeCard(card, playArea.getFreeCorners().get(0));

        assertTrue(playArea.getVisibleResources().get(LEAF) >= 5);

        GoldCard goldCard = new GoldCard(LEAF, 1,
                placementCost,
                new CornerCoverGoldCard(),
                new Corner(null, TL),
                new Corner(MUSHROOM, TR),
                new Corner(LEAF, BL)
        );
        // place card, assert no exception raised as placementCost is satisfied
        assertDoesNotThrow(()->playArea.placeCard(goldCard, playArea.getFreeCorners().get(0)));
    }

    @Test
    @DisplayName("Test placement of a GoldCard (placementCost not satisfied)")
    void testPlaceGoldCard_notOK() {
        setUp(false);

        Hashtable<GameResource, Integer> placementCost = new Hashtable<>();
        placementCost.put(LEAF, 5);
        GoldCard goldCard = new GoldCard(LEAF, 1,
                placementCost,
                new CornerCoverGoldCard(),
                new Corner(null, TL),
                new Corner(MUSHROOM, TR),
                new Corner(LEAF, BL)
        );

        //duplicate playArea to check for invariance after failed placeCard attempts
        Map<Point, PlaceableCard> oldCardMatrix = duplicateCardMatrix();

        assertThrows(RuntimeException.class, ()->playArea.placeCard(goldCard, playArea.getFreeCorners().get(0)));
        assertEquals(oldCardMatrix, playArea.getCardMatrix());
    }
}