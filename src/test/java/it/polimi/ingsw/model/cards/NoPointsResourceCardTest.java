package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.Hashtable;
import java.util.Map;

import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.GameResource.*;
import static org.junit.jupiter.api.Assertions.*;

class NoPointsResourceCardTest {

    private ResourceCard testCard;
    private static final Map<CornerDirection, Corner> card_corners = new Hashtable<>();
    private final GameResource backResource = BUTTERFLY;
    @BeforeAll
    public static void initializeCorners() {
        card_corners.put(TL, new Corner(QUILL, TL));
        card_corners.put(TR, new Corner(MUSHROOM, TR));
        card_corners.put(BL, new Corner(POTION, BL));
        card_corners.put(BR, new Corner(BUTTERFLY, BR));
    }

    @BeforeEach
    void setup(){
        try {
            testCard = new ResourceCard(backResource,
                    card_corners.get(TL),
                    // TR is filled
                    card_corners.get(BL),
                    card_corners.get(BR)
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }

        System.out.println("Start test...");
    }

    @Test
    @DisplayName("GetResources: facing up")
    void getCardResourcesUp() {
        testCard.turnFaceUp();
        Map<GameResource, Integer> resourceMap = testCard.getCardResources();
        assertEquals(1, resourceMap.get(POTION));
        assertEquals(1, resourceMap.get(BUTTERFLY));
        assertEquals(1, resourceMap.get(QUILL));
        for (GameResource res : GameResource.values()) {
            if (!res.equals(QUILL) && !res.equals(BUTTERFLY) && !res.equals(POTION) && !res.equals(FILLED)) {
                assertEquals(0, resourceMap.get(res));
            }
            if (res.equals(FILLED)) assertNull(resourceMap.get(res));
        }
    }

    @Test
    @DisplayName("GetResources: facing down")
    void getCardResourcesDown(){
        testCard.turnFaceDown();
        Map <GameResource, Integer> resourceMap = testCard.getCardResources();
        assertEquals(1, resourceMap.get(BUTTERFLY));
        for (GameResource res : GameResource.values()) {
            if (!res.equals(BUTTERFLY) && !res.equals(FILLED)) assertEquals(0, resourceMap.get(res));

            if(res.equals(FILLED)) assertNull(resourceMap.get(res));
        }
    }

    @Test
    void getPlacementCost() {
        Map<GameResource, Integer> placementCost = testCard.getPlacementCost();
        for(GameResource res: GameResource.values()) {
            assertNull(placementCost.get(res));
        }
    }

    @Test
    @DisplayName("Calculation on card with no points")
    void calculatePointsNoPoints() {
        assertEquals(0, testCard.calculatePointsOnPlace(null) );
    }

    @Test
    void getSetPosition() {
        // At instantiation position == null
        assertThrows(IllegalStateException.class, ()-> testCard.getPosition());

        GamePoint pos = new GamePoint(5, 5);
        ResourceCard placedCard = (ResourceCard) testCard.setPosition(pos);
        // After placement, new card will have updated position with no other changes
        assertEquals(pos, placedCard.getPosition());
        // Check that equals ignores position
        assertEquals(testCard, placedCard);
    }
}