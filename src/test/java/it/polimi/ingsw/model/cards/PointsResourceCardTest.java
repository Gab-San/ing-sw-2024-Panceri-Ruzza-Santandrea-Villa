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

public class PointsResourceCardTest {
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
            int pointsOnPlace = 419;
            testCard = new ResourceCard(backResource,
                    pointsOnPlace,
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
    void getPlacementCost() {
        Map<GameResource, Integer> placementCost = testCard.getPlacementCost();
        for(GameResource res: GameResource.values()) {
            assertNull(placementCost.get(res));
        }
    }

    @Test
    @DisplayName("Calculation on card with no points")
    void calculatePoints() {
        testCard.turnFaceUp();
        assertEquals(419, testCard.calculatePointsOnPlace(null) );
        assertNotEquals(418, testCard.calculatePointsOnPlace(null));
        testCard.turnFaceDown();
        assertEquals(0, testCard.calculatePointsOnPlace(null));
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
