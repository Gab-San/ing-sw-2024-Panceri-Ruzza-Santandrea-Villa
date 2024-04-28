package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.objective.PatternObjective;
import it.polimi.ingsw.model.cards.objective.PatternObjectiveStrategy;
import it.polimi.ingsw.model.cards.objective.ResourceObjectiveStrategy;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;

import static it.polimi.ingsw.model.enums.GameResource.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerHandTest {
    Player p = new Player("Test", null, 1);
    PlayerHand hand;
    PlayCard card1 = new ResourceCard(LEAF);
    PlayCard card2 = new ResourceCard(LEAF);
    PlayCard card3 = new ResourceCard(WOLF);
    PlayCard card4 = new ResourceCard(WOLF);

    @BeforeEach
    public void setupHand(){
        hand = new PlayerHand(p);
    }

    @Test
    public void containsCardTest(){
        assertEquals(card1, card2);
        assertNotSame(card1, card2);
        hand.addCard(card1);
        assertTrue(hand.containsCard(card1));
        assertFalse(hand.containsCard(card2));
        assertFalse(hand.containsCard(card3));
    }

    @Test
    public void addCardTest(){
        assertEquals(card1, card2);
        assertNotSame(card1, card2);
        hand.addCard(card1);
        hand.addCard(card2);
        assertThrows(PlayerHandException.class, ()->hand.addCard(card2));
        assertDoesNotThrow(()->hand.addCard(card3));
        assertThrows(PlayerHandException.class, ()->hand.addCard(card4));
    }

    @Test
    public void removeCardTest(){
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        assertTrue(hand.containsCard(card2));
        hand.removeCard(card2);
        assertFalse(hand.containsCard(card2));
        assertThrows(PlayerHandException.class, ()->hand.removeCard(card4));

        //testing list
        assertSame(hand.getCard(0), card1);
        assertSame(hand.getCard(1), card3);
        assertThrows(IndexOutOfBoundsException.class, ()->hand.getCard(2));
    }

    @Test
    public void popCardTest(){
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        assertSame(hand.popCard(1), card2);
        assertFalse(hand.containsCard(card2));
    }
    @Test
    public void secretObjectiveTest(){
        ObjectiveCard obj1 = new ObjectiveCard(new ResourceObjectiveStrategy(new Hashtable<>()), 1);
        ObjectiveCard obj1bis = new ObjectiveCard(new ResourceObjectiveStrategy(new Hashtable<>()), 1);
        ObjectiveCard obj2 = new ObjectiveCard(new PatternObjectiveStrategy(new PatternObjective("**B *B* B**")), 1);
        ObjectiveCard obj3 = new ObjectiveCard(new PatternObjectiveStrategy(new PatternObjective("**G *P* G**")), 1);
        assertThrows(PlayerHandException.class, ()->hand.getSecretObjective());

        hand.setCard(obj1);
        assertThrows(PlayerHandException.class, ()->hand.setCard(obj1));
        assertEquals(obj1, obj1bis);
        assertThrows(PlayerHandException.class, ()->hand.setCard(obj1bis));

        hand.setCard(obj2);
        assertThrows(PlayerHandException.class, ()->hand.setCard(obj3));
        assertEquals(2, hand.getObjectiveChoices().size());

        hand.chooseObjective(1);
        assertSame(hand.getSecretObjective(), obj1);
        assertEquals(1, hand.getObjectiveChoices().size());

        assertThrows(PlayerHandException.class, ()->hand.setCard(obj3));
    }

    @Test
    public void startingCardTest(){
        assertThrows(PlayerHandException.class, ()->hand.getStartingCard());

        StartingCard card = new StartingCard();
        assertDoesNotThrow(()->hand.setCard(card));
        assertSame(card, hand.getStartingCard());

        StartingCard card2 = new StartingCard(new GameResource[]{LEAF, WOLF});
        assertThrows(PlayerHandException.class, ()->hand.setCard(card2));
    }
}
