package it.polimi.ingsw.model.cards;

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

class PlayCardNoPointsTest {
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

//    @Test
//    void equalsTest(){
//        // Property: Reflexive;
//        assertEquals(testCard, testCard);
//
//        PlaceableCard testCardCopy = testCard;
//        assertEquals(testCard, testCardCopy);
//
//        // Symmetrical
//        PlaceableCard differentCard = null;
//        try {
//            differentCard = new ResourceCard(backResource, 0,
//                    card_corners.get(TL),
//                    card_corners.get(TR)
//                    // BR corner is filled
//                    // BL corner is filled
//            );
//        } catch (InvalidParameterException invalidParameterException){
//            fail("Resource card instantiation failed with error message: \n" + invalidParameterException.getMessage());
//        }
//
//        assertNotEquals(differentCard, testCard);
//        assertNotEquals(testCard, differentCard);
//
//        try {
//            differentCard = new ResourceCard(LEAF, 0,
//                    card_corners.get(TL),
//                    card_corners.get(TR),
//                    card_corners.get(BR)
//                    // BL corner is filled
//            );
//        } catch (InvalidParameterException ex) {
//            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
//        }
//
//        // Transitive
//        PlaceableCard anotherCard = null;
//        try {
//            anotherCard = new ResourceCard(LEAF, 0,
//                    card_corners.get(TL),
//                    card_corners.get(TR),
//                    card_corners.get(BR)
//                    // BL corner is filled
//            );
//        } catch (InvalidParameterException ex) {
//            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
//        }
//
//        if( testCard.equals(differentCard) && differentCard.equals(anotherCard) ){
//            assertEquals(testCard, anotherCard);
//        } else {
//            fail("Transitive property not met");
//        }
//    }

    @Test
    @DisplayName("Equal to self")
    void equalSelf(){
        //Property: Reflexive;
        assertEquals(testCard, testCard);

        PlaceableCard testCardCopy = testCard;
        assertEquals(testCard, testCardCopy);
    }

    @Test
    @DisplayName("Variable with same ref")
    void equalToSelfRef(){

    }

    @Test
    @DisplayName("Equal on different cards")
    void equalDifferentCards(){

    }
}