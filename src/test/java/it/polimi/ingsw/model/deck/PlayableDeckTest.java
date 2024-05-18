package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.deck.cardfactory.ResourceCardFactory;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlayableDeckTest {
    static PlayableDeck playableDeck;

    @BeforeAll
    static void setup() {
        try {
            playableDeck = new PlayableDeck(Board.RESOURCE_DECK, new ResourceCardFactory(),6);
        } catch (DeckInstantiationException deckExc) {
            deckExc.printStackTrace(System.err);
        }
    }

    @Test
    @DisplayName("THREAD TC1")
    void getTopCard() {
        if(playableDeck.isEmpty()){
            System.err.println("Deck is empty");
            return;
        }
        System.out.println(playableDeck.getTopCard());
    }

    @Test
    @DisplayName("THREAD TC2")
    void getTopCard2() {
        if(playableDeck.isEmpty()){
            System.err.println("Deck is empty");
            return;
        }

        System.out.println(playableDeck.getTopCard());
    }

    @Test
    @DisplayName("THREAD FR1")
    void getFirstRevealedCard() {
        if(playableDeck.isCompletelyEmpty()){
            System.err.println("Deck is empty");
            return;
        }
        System.out.println(playableDeck.getFirstRevealedCard());
    }

    @Test
    @DisplayName("THREAD FR2")
    void getFirstRevealedCard2() {
        if(playableDeck.isCompletelyEmpty()){
            System.err.println("Deck is empty");
            return;
        }
        System.out.println(playableDeck.getFirstRevealedCard());
    }

    @Test
    @DisplayName("THREAD SR1")
    void getSecondRevealedCard()  {
        if(playableDeck.isCompletelyEmpty()){
            System.err.println("Deck is empty");
            return;
        }

        System.out.println(playableDeck.getSecondRevealedCard());
    }

    @Test
    @DisplayName("THREAD SR2")
    void getSecondRevealedCard2() {
        if(playableDeck.isCompletelyEmpty()){
            System.err.println("Deck is empty");
            return;
        }

        System.out.println(playableDeck.getSecondRevealedCard());
    }

    @Test
    @DisplayName("THREAD SR3")
    void getSecondRevealedCard3() {
        if(playableDeck.isCompletelyEmpty()){
            System.err.println("Deck is empty");
            return;
        }
        System.out.println(playableDeck.getSecondRevealedCard());
    }

    @Test
    @DisplayName("THREAD FR3")
    void getFirstRevealedCard3() {
        if(playableDeck.isCompletelyEmpty()){
            System.err.println("Deck is empty");
            return;
        }
        System.out.println(playableDeck.getFirstRevealedCard());
    }

    @Test
    void drawTillEnd(){
        int counter = 0;
        while(true){
            if(playableDeck.isEmpty()){
                System.err.println("Deck is empty");
                return;
            }
            playableDeck.getTopCard();
            counter++;
            System.out.println(counter);
        }
    }
}