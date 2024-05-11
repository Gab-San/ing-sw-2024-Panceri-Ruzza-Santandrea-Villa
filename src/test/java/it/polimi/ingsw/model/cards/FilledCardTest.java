package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.model.enums.CornerDirection.*;
import static it.polimi.ingsw.model.enums.GameResource.*;
import static org.junit.jupiter.api.Assertions.*;

public class FilledCardTest {
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
            testCard = new ResourceCard(MUSHROOM);
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }
    }

    @Test
    void getCorner() {
        for(CornerDirection dir: CornerDirection.values()){
            // Only need to check if the corners have been copied correctly
            Corner corner = testCard.getCorner(dir);
            // manually check that the FILLED corner was created
            assertEquals(corner, new Corner(FILLED, null, dir));

            // These assert better test the corner
            assertTrue(corner.isVisible());
            assertFalse(corner.isOccupied());
        }
    }

    @Test
    @DisplayName("Free Corners: face up card")
    void getFreeCornersUP() {
        // Test that all corners are occupied
        testCard.turnFaceUp();
        List<Corner> freeCorners = testCard.getFreeCorners();
        for (CornerDirection dir : CornerDirection.values()) {
            boolean contained = freeCorners.contains(testCard.getCorner(dir));
            assertFalse(contained);
        }
        // Test that an occupied corner is removed from freeCorners
        Corner cornerTR = testCard.getCorner(TR);
        assertTrue(cornerTR.isOccupied());
        assertFalse(freeCorners.contains(cornerTR), "Occupied corner in freeCorners");
        cornerTR.occupy();
        assertTrue(cornerTR.isOccupied());
        assertFalse(testCard.getFreeCorners().contains(cornerTR), "Occupied corner IN freeCorners");
    }

    @Test
    @DisplayName("Free Corners: face down card")
    void getFreeCornersDown() {
        // Test that all corners are free at instantiation
        testCard.turnFaceDown();
        List<Corner> freeCorners = testCard.getFreeCorners();
        for (CornerDirection dir : CornerDirection.values()) {
            boolean contained = freeCorners.contains(testCard.getCorner(dir));
            assertTrue(contained);
        }
        // Test that an occupied corner is removed from freeCorners
        Corner cornerTR = testCard.getCorner(TR);
        assertFalse(cornerTR.isOccupied());
        assertTrue(freeCorners.contains(cornerTR), "Not occupied corner NOT in freeCorners");
        cornerTR.occupy();
        assertTrue(cornerTR.isOccupied());
        assertFalse(testCard.getFreeCorners().contains(cornerTR), "Occupied corner IN freeCorners");
    }

    @Test
    void getCornerResources() {
        int[] resourcesCount;

        // FRONT OF THE CARD
        testCard.turnFaceUp();

        resourcesCount = testCard.getCornerResources();

        for(GameResource res: GameResource.values()){
            if(!res.equals(FILLED)){
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

        Point pos = new Point(150, 200);
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
            differentCard = new ResourceCard(MUSHROOM,
                    card_corners.get(TL)
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
        PlaceableCard differentCard = null;
        try {
            differentCard = new ResourceCard(MUSHROOM);
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }

        assertEquals(differentCard,testCard);
        assertEquals(testCard,differentCard);
    }

    @Test
    @DisplayName("Equals: test if transitive")
    void equalTransitive(){
        PlaceableCard differentCard = null;
        try {
            differentCard = new ResourceCard(MUSHROOM);
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }

        PlaceableCard anotherCard = null;
        try {
            anotherCard = new ResourceCard(MUSHROOM);
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
