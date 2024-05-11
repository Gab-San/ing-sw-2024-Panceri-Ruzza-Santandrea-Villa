package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.deck.cardfactory.ResourceCardFactory;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlayableDeckTest {
    static PlayableDeck playableDeck;

    @BeforeAll
    static void setup() {
        try {
            playableDeck = new PlayableDeck(new ResourceCardFactory());
        } catch (DeckInstantiationException deckExc) {
            deckExc.printStackTrace(System.err);
        }
    }

    @Test
    @DisplayName("THREAD TC1")
    void getTopCard() {
        try {
            System.out.println(playableDeck.getTopCard());
        } catch (DeckException deckException) {
            if (deckException.getCause() != null) {
                System.err.println(deckException.getMessage());
            }

            deckException.printStackTrace(System.err);
        }
    }

    @Test
    @DisplayName("THREAD TC2")
    void getTopCard2() {
        try {
            System.out.println(playableDeck.getTopCard());
        } catch (DeckException deckException) {
            if (deckException.getCause() != null) {
                System.err.println(deckException.getMessage());
            }

            deckException.printStackTrace(System.err);
        }
    }

    @Test
    @DisplayName("THREAD FR1")
    void getFirstRevealedCard() {
        try{
            System.out.println(playableDeck.getFirstRevealedCard());
        } catch (DeckException ignored){
        }
    }

    @Test
    @DisplayName("THREAD FR2")
    void getFirstRevealedCard2() {
        try{
            System.out.println(playableDeck.getFirstRevealedCard());
        } catch (DeckException ignored){
        }
    }

    @Test
    @DisplayName("THREAD SR1")
    void getSecondRevealedCard()  {
        try{
            System.out.println(playableDeck.getSecondRevealedCard());
        } catch (DeckException ignored){
        }
    }

    @Test
    @DisplayName("THREAD SR2")
    void getSecondRevealedCard2() {
        try{
            System.out.println(playableDeck.getSecondRevealedCard());
        } catch (DeckException ignored){
        }
    }

    @Test
    @DisplayName("THREAD SR3")
    void getSecondRevealedCard3() {
        try{
            System.out.println(playableDeck.getSecondRevealedCard());
        } catch (DeckException ignored){
        }
    }

    @Test
    @DisplayName("THREAD FR3")
    void getFirstRevealedCard3() {
        try{
            System.out.println(playableDeck.getFirstRevealedCard());
        } catch (DeckException ignored){
        }
    }

    @Test
    void drawTillEnd(){
        int counter = 0;
        while(true){
            try {
                playableDeck.getTopCard();
                synchronized (playableDeck){
                    //With a 50 millisec sleep the deck is able to replenish
                    playableDeck.wait(50);
                }
                counter++;
                System.out.println(counter);
            } catch (DeckException deckException){
                System.err.println(deckException.getMessage());
                if(deckException.getCause() != null){
                    System.err.println(deckException.getCause().getMessage());
                }
                break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}