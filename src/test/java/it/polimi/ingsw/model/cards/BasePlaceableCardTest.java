package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.model.enums.CornerDirection.*;
import static it.polimi.ingsw.model.enums.CornerDirection.BR;
import static it.polimi.ingsw.model.enums.GameResource.*;
import static org.junit.jupiter.api.Assertions.*;

class BasePlaceableCardTest {
    private PlaceableCard testCard;
    private static final Map<CornerDirection, Corner> card_corners = new Hashtable<>();

    @BeforeAll
    public static void initializeCorners() {
        card_corners.put(TL, new Corner(null, TL));
        card_corners.put(TR, new Corner(LEAF, TR));
        card_corners.put(BR, new Corner(BUTTERFLY, BR));
        // BL is filled
    }
    @BeforeEach
    void setup(){
        try {
            testCard = new ResourceCard(LEAF, 0,
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
        for(CornerDirection dir: CornerDirection.values()){
            // Only need to check if the corners have been copied correctly
            Corner corner = testCard.getCorner(dir);
            if(dir != BL) {
                // card corners are equal to the corners
                assertEquals(card_corners.get(dir), corner);
            }else{
                // manually check that the FILLED corner was created
                assertEquals(corner, new Corner(FILLED, null, dir));
            }
            // These assert better test the corner
            assertTrue(corner.isVisible());
            assertFalse(corner.isOccupied());
        }
    }

    @Test
    void getFreeCorners() {
        // Test that all corners are free at instantiation
        List<Corner> freeCorners = testCard.getFreeCorners();
        for (CornerDirection dir : CornerDirection.values()) {
            boolean contained = freeCorners.contains(testCard.getCorner(dir));
            if (!testCard.getCorner(dir).isOccupied())
                assertTrue(contained);
            else
                assertFalse(contained);
        }
        // Test that an occupied corner is removed from freeCorners
        Corner cornerTR = testCard.getCorner(TR);
        assertFalse(cornerTR.isOccupied());
        // The displayed message is error message
        assertTrue(freeCorners.contains(cornerTR), "Not occupied and NOT in freeCorners");
        cornerTR.occupy();
        assertTrue(cornerTR.isOccupied());
        assertFalse(testCard.getFreeCorners().contains(cornerTR), "Occupied and IN freeCorners");
    }

    @Test
    void getCornerResources() {
        int[] resourcesCount;

        // FRONT OF THE CARD
        testCard.turnFaceUp();

        resourcesCount = testCard.getCornerResources();
        assertEquals(1, resourcesCount[LEAF.getResourceIndex()]);
        assertEquals(1, resourcesCount[BUTTERFLY.getResourceIndex()]);

        for(GameResource res: GameResource.values()){
            if(!res.equals(LEAF) && !res.equals(BUTTERFLY) && !res.equals(FILLED)){
                assertEquals(0 , resourcesCount[res.getResourceIndex()]);
            }
        }

        // BACK OF THE CARD

        testCard.turnFaceDown();
        resourcesCount = testCard.getCornerResources();

        for(GameResource res: GameResource.values()){
            if(!res.equals(FILLED)){
                assertEquals(0 , resourcesCount[res.getResourceIndex()]);
            }
        }

    }

    @Test
    void getSetPosition() {
        // At instantiation position == null
        assertThrows(RuntimeException.class, ()-> testCard.getPosition());

        Point pos = new Point(5, 5);
        PlaceableCard placedCard = testCard.setPosition(pos);
        // After placement, new card will have updated position with no other changes
        assertEquals(pos, placedCard.getPosition());
        // Check that equals ignores position
        assertEquals(testCard, placedCard);
    }

    @Test
    void equalsTest(){
        // Property: Reflexive;
        assertEquals(testCard, testCard);

        PlaceableCard testCardCopy = testCard;
        assertEquals(testCard, testCardCopy);

        // Symmetrical
        PlaceableCard differentCard = null;
        try {
            differentCard = new ResourceCard(LEAF, 0,
                    card_corners.get(TL),
                    card_corners.get(TR)
                    // BR corner is filled
                    // BL corner is filled
            );
        } catch (InvalidParameterException invalidParameterException){
            fail("Resource card instantiation failed with error message: \n" + invalidParameterException.getMessage());
        }

        assertNotEquals(differentCard, testCard);
        assertNotEquals(testCard, differentCard);

        try {
            differentCard = new ResourceCard(LEAF, 0,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BR)
                    // BL corner is filled
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }

        // Transitive
        PlaceableCard anotherCard = null;
        try {
            anotherCard = new ResourceCard(LEAF, 0,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BR)
                    // BL corner is filled
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }

        if( testCard.equals(differentCard) && differentCard.equals(anotherCard) ){
            assertEquals(testCard, anotherCard);
        } else {
            fail("Transitive property not met");
        }
    }
}