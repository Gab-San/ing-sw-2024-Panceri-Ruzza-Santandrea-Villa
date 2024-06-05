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

class BaseStartingCardTest {
    private StartingCard testCard;
    private static final GameResource[] centralRes = {WOLF, BUTTERFLY, LEAF};
    private static final Map<CornerDirection, Corner> card_corners = new Hashtable<>();

    @BeforeAll
    public static void initializeCorners() {
        card_corners.put(TL, new Corner(null, BUTTERFLY, TL));
        card_corners.put(TR, new Corner(null,MUSHROOM, TR));
        card_corners.put(BR, new Corner(FILLED, WOLF, BR));
        card_corners.put(BL, new Corner(FILLED, LEAF, BL));
    }

    @BeforeEach
    void setup(){
        try {
            testCard = new StartingCard(centralRes,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BR),
                    card_corners.get(BL)
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }
    }

    @Test
    @DisplayName("GetResources: facing up")
    void getCardResourcesUp() {
        testCard.turnFaceUp();
        final Map<GameResource, Integer> resourceMap = testCard.getCardResources();
        assertAll(
                () -> assertEquals(1, resourceMap.get(WOLF)),
                () -> assertEquals(1, resourceMap.get(BUTTERFLY)),
                () -> assertEquals(1, resourceMap.get(LEAF)),
                () -> {
                    for(GameResource res: GameResource.values()){
                        if(res != FILLED && res != WOLF && res != BUTTERFLY && res != LEAF){
                            assertEquals(0, resourceMap.get(res));
                        }
                        if(res == FILLED) assertNull(resourceMap.get(res));
                    }
                }
        );
    }

    @Test
    @DisplayName("GetResources: facing down")
    void getCardResourcesDown() {
        testCard.turnFaceDown();
        final Map<GameResource, Integer> resourceMap = testCard.getCardResources();
        assertAll(
                () -> assertEquals(1, resourceMap.get(WOLF)),
                () -> assertEquals(1, resourceMap.get(BUTTERFLY)),
                () -> assertEquals(1, resourceMap.get(LEAF)),
                () -> assertEquals(1, resourceMap.get(MUSHROOM)),
                () -> {
                    for(GameResource res: GameResource.values()){
                        if(res != FILLED && res != WOLF && res != BUTTERFLY && res != LEAF && res != MUSHROOM){
                            assertEquals(0, resourceMap.get(res));
                        }
                        if(res == FILLED) assertNull(resourceMap.get(res));
                    }
                }
        );
    }

    @Test
    void getCardColour() {
        assertNull(testCard.getCardColour());
    }

    @Test
    void getSetPosition() {
        // At instantiation position == null
        assertThrows(IllegalStateException.class, ()-> testCard.getPosition());

        GamePoint pos = new GamePoint(5, 5);
        StartingCard placedCard = (StartingCard) testCard.setPosition(pos);
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

        StartingCard testCardCopy = testCard;
        assertEquals(testCard, testCardCopy);
    }

    @Test
    @DisplayName("Equals: vars with same ref")
    void equalToSelfRef(){
        StartingCard testCardCopy = testCard;
        assertEquals(testCardCopy, testCard);
    }


    @Test
    @DisplayName("Equals: differ central resources")
    void equalDifferent(){
        StartingCard differentCard = null;
        GameResource[] centralResources = {WOLF, BUTTERFLY};
        try {
            differentCard = new StartingCard(centralResources,
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
    @DisplayName("Equals: differ central resources")
    void equalDifferent1(){
        StartingCard differentCard = null;
        GameResource[] centralResources = {WOLF, BUTTERFLY, MUSHROOM};
        try {
            differentCard = new StartingCard(centralResources,
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
    @DisplayName("Equals: differ by one corner")
    void equalDifferent2(){
        StartingCard differentCard = null;
        try {
            differentCard = new StartingCard(centralRes,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BR)
                    // BL is filled;
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }

        assertNotEquals(differentCard, testCard);
        assertNotEquals(testCard, differentCard);
    }

    @Test
    @DisplayName("Equals: differ by subtype")
    void equalDifferent3(){
        ResourceCard resCard = new ResourceCard(
                LEAF,
                2,
                card_corners.get(TR),
                card_corners.get(BR)
        );

        assertNotEquals(testCard,resCard);
    }

    @Test
    @DisplayName("Equals: equal cards")
    void equalEqual(){
        StartingCard differentCard = null;
        try {
            differentCard = new StartingCard(centralRes,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BR),
                    card_corners.get(BL)
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
        StartingCard differentCard = null;
        try {
            differentCard = new StartingCard(centralRes,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BR),
                    card_corners.get(BL)
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }

        StartingCard anotherCard = null;
        try {
            anotherCard = new StartingCard(centralRes,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BR),
                    card_corners.get(BL)
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