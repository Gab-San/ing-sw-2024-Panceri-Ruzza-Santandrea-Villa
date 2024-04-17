package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardTest {
    Card testCard;
    @BeforeEach
    void setup(){
        testCard = new ResourceCard();
        System.out.println("\nStarting test...\n");
    }
    @Test
    void flip() {
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
        // Reflexive
        assertTrue(testCard.equals(testCard));
        Card newCard = testCard;
        // Symmetrical
        assertTrue(testCard.equals((Card) newCard));
        assertTrue(newCard.equals( (Card) testCard));
        Corner notCard = new Corner(GameResource.BUTTERFLY, CornerDirection.TL);
        assertFalse(testCard.equals(notCard));
        newCard = new ResourceCard();
        newCard.turnFaceUp();
        testCard.turnFaceUp();
        assertTrue(testCard.equals(newCard));
        assertTrue(newCard.equals(testCard));
        testCard.turnFaceDown();
        assertFalse(testCard.equals(newCard));
        assertFalse(newCard.equals(testCard));
        newCard.turnFaceDown();
        assertTrue(testCard.equals(newCard));
        assertTrue(newCard.equals(testCard));
    }
}