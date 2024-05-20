package it.polimi.ingsw.model;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.cardstrategies.CornerCoverGoldCard;
import it.polimi.ingsw.model.enums.GameResource;
import org.jetbrains.annotations.NotNull;
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
        assertEquals(2, playArea.getFreeCorners().size());
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
        assertEquals(4, playArea.getFreeCorners().size());
    }
    @Test
    @DisplayName("Test failure on placement of a second starting card (first placement on both sides)")
    void testPlaceStartingCard_fail() {
        setUp(false);
        assertThrows(IllegalStateException.class, ()->playArea.placeStartingCard(startingCard));
        setUp(true);
        assertThrows(IllegalStateException.class, ()->playArea.placeStartingCard(startingCard));
    }
    @Test
    @DisplayName("Test placement of a PlayCard")
    // TODO: duplicate this test placing card on back
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

        //assert other placed card's corners are not occupied and visible
        assertEquals(3, cardPlaced.getFreeCorners().size());
        assertFalse(cardPlaced.getFreeCorners().contains(placedCard_corner));

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
        assertEquals(startingCard.getFreeCorners(), playArea.getFreeCorners());
        assertEquals(3, playArea.getFreeCorners().size());

        //duplicate playArea to check for invariance after failed placeCard attempts
        Map<Point, PlaceableCard> oldCardMatrix = duplicateCardMatrix();

        PlayCard cardToPlace = new ResourceCard();
        assertThrows(IllegalStateException.class,
                ()->playArea.placeCard(cardToPlace, blockedCard.getCorner(BL))
        );
        assertEquals(oldCardMatrix, playArea.getCardMatrix());
        assertThrows(IllegalStateException.class,
                ()->playArea.placeCard(cardToPlace, blockedCard.getCorner(TR))
        );
        assertEquals(oldCardMatrix, playArea.getCardMatrix());
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
        assertThrows(IllegalStateException.class,
                ()->playArea.placeCard(otherCard, startCardCorner));
        assertEquals(oldCardMatrix, playArea.getCardMatrix());

        // test of failure on occupied visible corner
        assertThrows(IllegalStateException.class,
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
        goldCard.turnFaceUp();
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
        goldCard.turnFaceUp();

        //duplicate playArea to check for invariance after failed placeCard attempts
        Map<Point, PlaceableCard> oldCardMatrix = duplicateCardMatrix();

        assertThrows(IllegalStateException.class, ()->playArea.placeCard(goldCard, playArea.getFreeCorners().get(0)));
        assertEquals(oldCardMatrix, playArea.getCardMatrix());
    }

    @Test
    @DisplayName("Test covering of multiple corners (left side)")
    void testPlaceCard_multipleCoveredCorners_left() {
        setUp(false);
        ResourceCard cardTL = new ResourceCard();
        ResourceCard cardBL = new ResourceCard();
        playArea.placeCard(cardTL, startingCard.getCorner(TL));
        playArea.placeCard(cardBL, startingCard.getCorner(BL));
        ResourceCard card = new ResourceCard();
        playArea.placeCard(card, cardBL.getCorner(TL));

        assertTrue(cardBL.getCorner(TL).isOccupied());
        assertFalse(cardBL.getCorner(TL).isVisible());
        assertTrue(cardTL.getCorner(BL).isOccupied());
        assertFalse(cardTL.getCorner(BL).isVisible());
        assertFalse(playArea.getFreeCorners().contains(card.getCorner(TR)));
        assertFalse(playArea.getFreeCorners().contains(card.getCorner(BR)));
    }
    @Test
    @DisplayName("Test covering of multiple corners (right side)")
    void testPlaceCard_multipleCoveredCorners_right() {
        setUp(false);
        ResourceCard cardTR = new ResourceCard();
        ResourceCard cardBR = new ResourceCard();
        playArea.placeCard(cardTR, startingCard.getCorner(TR));
        playArea.placeCard(cardBR, startingCard.getCorner(BR));
        ResourceCard card = new ResourceCard();
        playArea.placeCard(card, cardBR.getCorner(TR));

        assertTrue(cardBR.getCorner(TR).isOccupied());
        assertFalse(cardBR.getCorner(TR).isVisible());
        assertTrue(cardTR.getCorner(BR).isOccupied());
        assertFalse(cardTR.getCorner(BR).isVisible());
        assertFalse(playArea.getFreeCorners().contains(card.getCorner(TL)));
        assertFalse(playArea.getFreeCorners().contains(card.getCorner(BL)));
    }
    @Test
    @DisplayName("Test covering of multiple corners (top side)")
    void testPlaceCard_multipleCoveredCorners_top() {
        setUp(false);
        ResourceCard cardTR = new ResourceCard();
        ResourceCard cardTL = new ResourceCard();
        playArea.placeCard(cardTR, startingCard.getCorner(TR));
        playArea.placeCard(cardTL, startingCard.getCorner(TL));
        ResourceCard card = new ResourceCard();
        playArea.placeCard(card, cardTL.getCorner(TR));

        assertTrue(cardTL.getCorner(TR).isOccupied());
        assertFalse(cardTL.getCorner(TR).isVisible());
        assertTrue(cardTR.getCorner(TL).isOccupied());
        assertFalse(cardTR.getCorner(TL).isVisible());
        assertFalse(playArea.getFreeCorners().contains(card.getCorner(BR)));
        assertFalse(playArea.getFreeCorners().contains(card.getCorner(BL)));
    }
    @Test
    @DisplayName("Test covering of multiple corners (bottom side)")
    void testPlaceCard_multipleCoveredCorners_bottom() {
        setUp(false);
        PlayCard cardBL = new ResourceCard();
        PlayCard cardBR = new ResourceCard();
        playArea.placeCard(cardBL, startingCard.getCorner(BL));
        cardBL = (PlayCard) cardBL.getCorner(TL).getCardRef();
        playArea.placeCard(cardBR, startingCard.getCorner(BR));
        cardBR = (PlayCard) cardBR.getCorner(TL).getCardRef();
        PlayCard  card = new ResourceCard();
        playArea.placeCard(card, cardBR.getCorner(BL));
        card = (PlayCard) card.getCorner(TL).getCardRef();

        assertTrue(cardBR.getCorner(BL).isOccupied());
        assertFalse(cardBR.getCorner(BL).isVisible());
        assertTrue(cardBL.getCorner(BR).isOccupied());
        assertFalse(cardBL.getCorner(BR).isVisible());
        assertFalse(playArea.getFreeCorners().contains(card.getCorner(TL)));
        assertFalse(playArea.getFreeCorners().contains(card.getCorner(TR)));
    }
}