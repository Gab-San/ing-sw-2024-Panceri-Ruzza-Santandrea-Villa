package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.Hashtable;
import java.util.Map;

import static it.polimi.ingsw.model.enums.CornerDirection.*;
import static it.polimi.ingsw.model.enums.CornerDirection.BR;
import static it.polimi.ingsw.model.enums.GameResource.*;
import static org.junit.jupiter.api.Assertions.*;

class NoPointsPlayCardTest {
    private PlayCard testCard;
    private static final Map<CornerDirection, Corner> card_corners = new Hashtable<>();
    private final GameResource backResource = BUTTERFLY;
    @BeforeAll
    public static void initializeCorners() {
        card_corners.put(TL, new Corner(QUILL, TL));
        card_corners.put(TR, new Corner(MUSHROOM, TR));
        card_corners.put(BL, new Corner(BUTTERFLY, BL));
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
    public void getCardResources() {
        Map<GameResource, Integer> resourceMap;

        // FRONT
        testCard.turnFaceUp();
        resourceMap = testCard.getCardResources();
        assertEquals(1, resourceMap.get(QUILL));
        assertEquals(2, resourceMap.get(BUTTERFLY));
        for (GameResource res : GameResource.values()) {
            if (!res.equals(QUILL) && !res.equals(BUTTERFLY) && !res.equals(FILLED)) {
                assertEquals(0, resourceMap.get(res));
            }
            if(res.equals(FILLED)) assertNull(resourceMap.get(res));
        }

        // BACK
        testCard.turnFaceDown();
        resourceMap = testCard.getCardResources();
        assertEquals(1, resourceMap.get(BUTTERFLY));
        for (GameResource res : GameResource.values()) {
            if (!res.equals(BUTTERFLY) && !res.equals(FILLED)) assertEquals(0, resourceMap.get(res));

            if(res.equals(FILLED)) assertNull(resourceMap.get(res));
        }
    }

    @Test
    void getCardColour() {
        assertEquals(backResource, testCard.getCardColour());
    }

    @Test
    void getSetPosition() {
        // At instantiation position == null
        assertThrows(IllegalStateException.class, ()-> testCard.getPosition());

        Point pos = new Point(5, 5);
        PlayCard placedCard = (PlayCard) testCard.setPosition(pos);
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

        PlayCard testCardCopy = testCard;
        assertEquals(testCard, testCardCopy);
    }

    @Test
    @DisplayName("Equals: vars with same ref")
    void equalToSelfRef(){
        PlayCard testCardCopy = testCard;
        assertEquals(testCardCopy, testCard);
    }

    @Test
    @DisplayName("Equals: different back resource")
    void equalDifferent(){
        PlayCard differentCard = null;
        try {
            differentCard = new ResourceCard(
                    WOLF,
                    card_corners.get(TL),
                    // TR is filled
                    card_corners.get(BL),
                    card_corners.get(BR)
            );

        } catch(InvalidParameterException invalidParameterException){
            fail("Resource card instantiation failed with error message: \n" + invalidParameterException.getMessage());
        }

        assertNotEquals(differentCard, testCard);
        assertNotEquals(testCard, differentCard);
    }

    @Test
    @DisplayName("Equals: equal cards")
    void equalEqual(){
        PlayCard differentCard = null;
        try {
            differentCard = new ResourceCard(backResource,
                    card_corners.get(TL),
                    // TR is filled
                    card_corners.get(BL),
                    card_corners.get(BR)
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
            differentCard = new ResourceCard(backResource,
                    card_corners.get(TL),
                    // TR is filled
                    card_corners.get(BL),
                    card_corners.get(BR)
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }

        PlayCard anotherCard = null;
        try {
            anotherCard = new ResourceCard(backResource,
                    card_corners.get(TL),
                    // TR is filled
                    card_corners.get(BL),
                    card_corners.get(BR)
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