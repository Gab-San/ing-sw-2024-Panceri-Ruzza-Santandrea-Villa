package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

//TODO redo corner test of resource
class BaseCornerTest {
    Corner cornerTL = new Corner(GameResource.BUTTERFLY, CornerDirection.TL);
    Corner cornerTR = new Corner(GameResource.BUTTERFLY, CornerDirection.TR);
    Corner cornerBL = new Corner(null, CornerDirection.BL);

    PlaceableCard testCard;

    @BeforeEach
    void printNewLine(){
        System.out.println();
    }
    @BeforeEach
    void setup(){
        testCard = new ResourceCard(GameResource.BUTTERFLY, cornerTL, cornerBL, cornerTR);
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
    void getResource() {

        for(CornerDirection cornerDirection : CornerDirection.values()){
            System.out.println("Testing "+ cornerDirection + " ...");
            GameResource cornRes = testCard.getCorner(cornerDirection).getResource();
            if(cornRes != null) {
                System.out.println(cornerDirection + " " + cornRes);
            } else {
                System.out.println("This corner is empty");
            }
        }
    }

     //TODO test equals
}