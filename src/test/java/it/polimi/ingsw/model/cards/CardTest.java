package it.polimi.ingsw.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.*;

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
}