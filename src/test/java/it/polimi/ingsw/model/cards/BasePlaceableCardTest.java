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
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.CornerDirection.BR;
import static it.polimi.ingsw.GameResource.*;
import static org.junit.jupiter.api.Assertions.*;

class BasePlaceableCardTest {
    private PlaceableCard testCard;
    private static final Map<CornerDirection, Corner> card_corners = new Hashtable<>();

    @BeforeAll
    public static void initializeCorners() {
        card_corners.put(TL, new Corner(null, TL));
        card_corners.put(TR, new Corner(LEAF, TR));
        card_corners.put(BR, new Corner(BUTTERFLY, BR));
        card_corners.put(BL, new Corner(null, BL));
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
    @DisplayName("Free Corners: face up card")
    void getFreeCornersUp() {
        // Test that all corners are free at instantiation
        testCard.turnFaceUp();
        List<Corner> freeCorners = testCard.getFreeCorners();
        for (CornerDirection dir : CornerDirection.values()) {
            boolean contained = freeCorners.contains(testCard.getCorner(dir));
            if (dir != BL){
                assertTrue(contained);
            } else {
                assertFalse(contained);
            }
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
    @DisplayName("Free Corners: face down card")
    void getFreeCornersDown() {
        // Test that all corners are free at instantiation
        List<Corner> freeCorners = testCard.getFreeCorners();
        for (CornerDirection dir : CornerDirection.values()) {
            assertTrue(freeCorners.contains(testCard.getCorner(dir)));
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
        assertThrows(IllegalStateException.class, ()-> testCard.getPosition());

        GamePoint pos = new GamePoint(5, 5);
        PlaceableCard placedCard = testCard.setPosition(pos);
        // After placement, new card will have updated position with no other changes
        assertEquals(pos, placedCard.getPosition());
        // Check that equals ignores position
        assertEquals(testCard, placedCard);
    }

    @Test
    @DisplayName("Equals: self")
    void equalSelf(){
        //Property: Reflexive;
        assertEquals(testCard, testCard);

        PlaceableCard testCardCopy = testCard;
        assertEquals(testCard, testCardCopy);
    }

    @Test
    @DisplayName("Equals: vars with same ref")
    void equalToSelfRef(){
        PlaceableCard testCardCopy = testCard;
        assertEquals(testCardCopy, testCard);
    }


    @Test
    @DisplayName("Equals: differ by one corner")
    void equalDifferent1(){
        PlaceableCard differentCard = null;
        try {
            differentCard = new ResourceCard(LEAF, 0,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BR),
                    card_corners.get(BL)
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }

        assertNotEquals(differentCard, testCard);
        assertNotEquals(testCard, differentCard);
    }

    @Test
    @DisplayName("Equals: differ by one corner resource")
    void equalDifferent2(){
        PlaceableCard differentCard = null;
        try {
            differentCard = new ResourceCard(LEAF, 0,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    new Corner(BUTTERFLY, MUSHROOM, BR)
                    // BL corner is filled
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }
        assertNotEquals(differentCard, testCard);
        assertNotEquals(testCard, differentCard);
    }



    @Test
    @DisplayName("Equals: equal cards")
    void equalEqual(){
        PlayCard differentCard = null;
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

        assertEquals(differentCard,testCard);
        assertEquals(testCard,differentCard);
    }

    @Test
    @DisplayName("Equals: test if transitive")
    void equalTransitive(){
        PlayCard differentCard = null;
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

        PlayCard anotherCard = null;
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