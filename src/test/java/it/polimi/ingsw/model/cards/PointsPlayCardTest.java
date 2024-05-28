package it.polimi.ingsw.model.cards;

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
import static it.polimi.ingsw.CornerDirection.BR;
import static it.polimi.ingsw.GameResource.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class PointsPlayCardTest {
    private PlayCard testCard;
    private static final Map<CornerDirection, Corner> card_corners = new Hashtable<>();
    private final GameResource backResource = WOLF;
    @BeforeAll
    public static void initializeCorners() {
        card_corners.put(TL, new Corner(WOLF, TL));
        card_corners.put(TR, new Corner(WOLF, TR));
        card_corners.put(BL, new Corner(SCROLL, BL));
        card_corners.put(BR, new Corner(WOLF, BR));
    }

    @BeforeEach
    void setup(){
        try {
            testCard = new ResourceCard(backResource,
                    3,
                    card_corners.get(TL),
                    card_corners.get(TR),
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
        assertEquals(1, resourceMap.get(SCROLL));
        assertEquals(3, resourceMap.get(WOLF));
        for (GameResource res : GameResource.values()) {
            if (!res.equals(SCROLL) && !res.equals(WOLF) && !res.equals(FILLED)) {
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
        assertEquals(1, resourceMap.get(WOLF));
        for (GameResource res : GameResource.values()) {
            if (!res.equals(WOLF) && !res.equals(FILLED)) assertEquals(0, resourceMap.get(res));

            if(res.equals(FILLED)) assertNull(resourceMap.get(res));
        }
    }

    @Test
    void getCardColour() {
        assertEquals(backResource, testCard.getCardColour());
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
                    LEAF,
                    3,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BL),
                    card_corners.get(BR)
            );

        } catch(InvalidParameterException invalidParameterException){
            fail("Resource card instantiation failed with error message: \n" + invalidParameterException.getMessage());
        }

        assertNotEquals(differentCard, testCard, "Doesn't catch difference on res");
        assertNotEquals(testCard, differentCard);
    }

    @Test
    @DisplayName("Equals: differ by points on placement")
    void equalDifferent1(){
        PlayCard differentCard = null;
        try {
            differentCard = new ResourceCard(
                    backResource,
                    21,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BL),
                    card_corners.get(BR)
            );

        } catch(InvalidParameterException invalidParameterException){
            fail("Resource card instantiation failed with error message: \n" + invalidParameterException.getMessage());
        }

        assertNotEquals(differentCard, testCard, "Doesn't catch difference on point on placement");
        assertNotEquals(testCard, differentCard);
    }

    @Test
    @DisplayName("Equals: equal cards")
    void equalEqual(){
        PlayCard differentCard = null;
        try {
            differentCard = new ResourceCard(backResource,
                    3,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BL),
                    card_corners.get(BR)
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }

        assertEquals(differentCard,testCard, "Doesn't recognise equal cards");
        assertEquals(testCard,differentCard);
    }

    @Test
    @DisplayName("Equals: test if transitive")
    void equalTransitive(){
        PlayCard differentCard = null;
        try {
            differentCard = new ResourceCard(backResource,
                    3,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BL),
                    card_corners.get(BR)
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }

        PlayCard anotherCard = null;
        try {
            anotherCard = new ResourceCard(backResource,
                    3,
                    card_corners.get(TL),
                    card_corners.get(TR),
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
