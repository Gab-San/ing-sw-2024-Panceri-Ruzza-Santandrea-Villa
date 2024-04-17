package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.model.enums.CornerDirection.*;
import static it.polimi.ingsw.model.enums.GameResource.*;
import static org.junit.jupiter.api.Assertions.*;

class PlaceableCardTest extends CardTest {
    PlaceableCard card = null;
    Map<CornerDirection, Corner> card_corners = new Hashtable<>();

    PlaceableCardTest() {
        card_corners.put(TL, new Corner(null, TL));
        card_corners.put(TR, new Corner(LEAF, TR));
        card_corners.put(BR, new Corner(BUTTERFLY, BR));
        // BL is filled
    }

    @BeforeEach
    public void placeableCard_instantiate() {
        try {
            card = new ResourceCard(LEAF, 0,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BR)
                    // BL corner is filled
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }
    }

    @Test
    void getCorner() {
        // check correct corner instantiation (only check one card as they all call PlaceableCard)
        for (CornerDirection dir : CornerDirection.values()) {
            Corner corner = card.getCorner(dir);
            assertEquals(corner.getCardRef(), card);
            if (dir != BL)
                assertEquals(corner, card_corners.get(dir));
            else
                assertEquals(corner, new Corner(FILLED, null, dir)); // manually check that the FILLED corner was created
            // These assert better test the corner
            assertTrue(corner.isVisible());
            assertFalse(corner.isOccupied());
        }
    }

    @Test
    public void getCardColour() {
        //check correct card color
        assertEquals(card.getCardColour(), LEAF);
    }

    @Test
    public void getCardResources() {
        Map<GameResource, Integer> resourceMap;
        //check getCardResources method (both on card front and back)
        card.turnFaceUp();
        // FRONT
        resourceMap = card.getCardResources();
        assertEquals(1, resourceMap.get(LEAF));
        assertEquals(1, resourceMap.get(BUTTERFLY));
        for (GameResource res : GameResource.values()) {
            if (!res.equals(LEAF) && !res.equals(BUTTERFLY) && !res.equals(FILLED)) {
                assertEquals(0, resourceMap.get(res));
            }
            if(res.equals(FILLED)) assertNull(resourceMap.get(res));
        }

        card.turnFaceDown();
        // BACK
        resourceMap = card.getCardResources();
        assertEquals(1, resourceMap.get(LEAF));
        for (GameResource res : GameResource.values()) {
            if (!res.equals(LEAF) && !res.equals(FILLED))
                assertEquals(0, resourceMap.get(res));

            if(res.equals(FILLED)) assertNull(resourceMap.get(res));
        }
    }

    @Test
    void getFreeCorners() {
        //test that all corners are free at instantiation
        List<Corner> freeCorners = card.getFreeCorners();
        for (CornerDirection dir : CornerDirection.values()) {
            boolean contained = freeCorners.contains(card.getCorner(dir));
            if (!card.getCorner(dir).isOccupied())
                assertTrue(contained);
            else
                assertFalse(contained);
        }
        //test that an occupied corner is removed from freeCorners
        Corner cornerTR = card.getCorner(TR);
        assertFalse(cornerTR.isOccupied());
        assertTrue(freeCorners.contains(cornerTR), "not occupied and in freeCorners");
        cornerTR.occupy();
        assertTrue(cornerTR.isOccupied());
        assertFalse(card.getFreeCorners().contains(cornerTR), "occupied and not in freeCorners");
    }

    @Test
    void getSetPosition() {
        // at instantiation position == null
        assertNull(card.getPosition());

        Point pos = new Point(5, 5);
        PlaceableCard placedCard = card.setPosition(pos);
        // after placement, new card will have updated position with no other changes
        assertEquals(pos, placedCard.getPosition());
        assertEquals(card, placedCard); // equals ignores position
    }
}