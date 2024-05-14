package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.CornerDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static it.polimi.ingsw.model.enums.GameResource.BUTTERFLY;
import static it.polimi.ingsw.model.enums.GameResource.FILLED;
import static org.junit.jupiter.api.Assertions.*;

class BaseCornerTest {
    Corner cornerTL = new Corner(BUTTERFLY, CornerDirection.TL);
    Corner cornerTR = new Corner(BUTTERFLY, CornerDirection.TR);
    Corner cornerBL = new Corner(null, CornerDirection.BL);

    PlaceableCard testCard;

    @BeforeEach
    void printNewLine(){
        System.out.println();
    }
    @BeforeEach
    void setup(){
        testCard = new ResourceCard(BUTTERFLY, cornerTL, cornerBL, cornerTR);
        testCard.turnFaceUp();
        cornerTL = testCard.getCorner(CornerDirection.TL);
        cornerTR = testCard.getCorner(CornerDirection.TR);
        cornerBL = testCard.getCorner(CornerDirection.BL);
    }
    @Test
    void getCardRef() {
        assertEquals(testCard, cornerTL.getCardRef());
    }

    @Test
    void getDirection() {
        for(CornerDirection cornerDirection: CornerDirection.values()) {
            System.out.println("Testing " + cornerDirection + " ...");
            try{
                assertEquals(cornerDirection, testCard.getCorner(cornerDirection).getDirection());
            } catch (NoSuchElementException filledCorner){
                System.err.println("This corner is filled");
            }
        }
    }

    @Test
    void occupy() {
        assertFalse(cornerBL.isOccupied());
        cornerBL.occupy();
        assertTrue(cornerBL.isOccupied());
    }

    @Test
    void cover() {
        assertTrue(cornerTL.isVisible());
        cornerTL.cover();
        assertFalse(cornerTL.isVisible());
    }

    @Test
    void getResourceFront() {
        testCard.turnFaceUp();
        assertAll(
                () -> assertEquals(BUTTERFLY, testCard.getCorner(CornerDirection.TL).getResource()),
                () -> assertEquals(BUTTERFLY, testCard.getCorner(CornerDirection.TR).getResource()),
                () -> assertNull(testCard.getCorner(CornerDirection.BL).getResource()),
                () -> assertEquals(FILLED, testCard.getCorner(CornerDirection.BR).getResource() )
        );
    }

    @Test
    void getResourceBack(){
        testCard.turnFaceDown();
        for(CornerDirection cornerDirection : CornerDirection.values()){
            System.out.println("Testing "+ cornerDirection + " ...");
            assertNull(testCard.getCorner(cornerDirection).getResource());
        }
    }
}