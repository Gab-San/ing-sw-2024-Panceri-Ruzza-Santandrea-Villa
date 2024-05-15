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
import static it.polimi.ingsw.GameResource.*;
import static it.polimi.ingsw.GameResource.MUSHROOM;
import static org.junit.jupiter.api.Assertions.*;

class DoubleSideResStartingCardTest {
    private StartingCard testCard;
    private static final GameResource[] centralRes = {MUSHROOM};
    private static final Map<CornerDirection, Corner> card_corners = new Hashtable<>();

    @BeforeAll
    public static void initializeCorners() {
        card_corners.put(TL, new Corner(WOLF, LEAF, TL));
        card_corners.put(TR, new Corner(null,WOLF, TR));
        card_corners.put(BR, new Corner(MUSHROOM, BUTTERFLY, BR));
        card_corners.put(BL, new Corner(null, MUSHROOM, BL));
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
                () -> assertEquals(2, resourceMap.get(MUSHROOM)),
                () -> {
                    for(GameResource res: GameResource.values()){
                        if(res != FILLED && res != WOLF && res != MUSHROOM){
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
    @DisplayName("Equals: differ by one corner resource")
    void equalDifferent1(){
        StartingCard differentCard = null;
        try {
            differentCard = new StartingCard(centralRes,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BR),
                    new Corner(null, BUTTERFLY, BL)
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
        StartingCard differentCard = null;
        try {
            differentCard = new StartingCard(centralRes,
                    card_corners.get(TL),
                    card_corners.get(TR),
                    card_corners.get(BR),
                    new Corner(FILLED, BUTTERFLY, BL)
            );
        } catch (InvalidParameterException ex) {
            fail("Resource card instantiation failed with error message: \n" + ex.getMessage());
        }

        assertNotEquals(differentCard, testCard);
        assertNotEquals(testCard, differentCard);
    }

}