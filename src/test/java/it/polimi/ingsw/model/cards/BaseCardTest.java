package it.polimi.ingsw.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BaseCardTest {
    private Card testCard;
    @BeforeEach
    void setup(){
        System.out.println("\nStarting Test...\n");
        setupCard(new ResourceCard());
    }

    void setupCard(Card card){
        testCard = card;
    }

    @Test
    void flip(){
        assertFalse(testCard.isFaceUp());
        System.out.println(testCard.isFaceUp ? "Face Up" : "Face Down");

        testCard.flip();
        System.out.println("Flipping...");

        assertTrue(testCard.isFaceUp());
        System.out.println(testCard.isFaceUp ? "Face Up" : "Face Down");

        testCard.flip();
        System.out.println("Flipping...");

        assertFalse(testCard.isFaceUp());
        System.out.println(testCard.isFaceUp ? "Face Up" : "Face Down");
    }

    @Test
    void turnFaceUp() {
        assertFalse(testCard.isFaceUp());

        testCard.turnFaceUp();
        assertTrue(testCard.isFaceUp());

        testCard.turnFaceUp();
        assertTrue(testCard.isFaceUp());
        System.out.println(testCard.isFaceUp ? "Face Up" : "Face Down");
    }

    @Test
    void turnFaceDown() {
        testCard.turnFaceDown();
        assertFalse(testCard.isFaceUp());

        testCard.flip();
        assertTrue(testCard.isFaceUp());

        testCard.turnFaceDown();
        assertFalse(testCard.isFaceUp());
        System.out.println(testCard.isFaceUp ? "Face Up" : "Face Down");
    }

    @Test
    void isFaceUp() {
        if(testCard.isFaceUp() != testCard.isFaceUp) throw new AssertionError();
    }

    @Test
    void equalsTest(){
        // Property: Reflexive;
        assertEquals(testCard, testCard);

        Card testCardCopy = testCard;
        assertEquals(testCard, testCardCopy);

        // Symmetrical
        final Card differentCard = new ResourceCard();

        differentCard.turnFaceUp();
        testCard.turnFaceUp();

        assertAll(
                () -> assertEquals(testCard, differentCard),
                () -> assertEquals(differentCard, testCard)
        );

        testCard.turnFaceDown();
        assertAll(
                () -> assertEquals(testCard, testCardCopy),
                () -> assertNotEquals(testCard, differentCard),
                () -> assertNotEquals(differentCard, testCard)
        );

        differentCard.turnFaceDown();

        assertAll(
                () -> assertEquals(testCard, differentCard),
                () -> assertEquals(differentCard, testCard)
        );

        // Transitive
        final Card anotherCard = new ResourceCard();
        anotherCard.turnFaceDown();

        if( testCard.equals(differentCard) && differentCard.equals(anotherCard) ){
            assertEquals(testCard, anotherCard);
        } else {
            fail("Transitive property not met");
        }
    }
}
