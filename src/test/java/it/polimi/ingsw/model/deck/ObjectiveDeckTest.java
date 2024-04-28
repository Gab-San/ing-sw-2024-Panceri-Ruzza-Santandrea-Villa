package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ObjectiveDeckTest {
    static ObjectiveDeck objDeck;
    @BeforeAll
    public static void instatiateDeck() {
        try {
            objDeck = new ObjectiveDeck();
        } catch (DeckInstantiationException deckException){
            deckException.printStackTrace(System.err);
        }
    }

    @Test
    void getCard() {
        while (true){
            try {
                ObjectiveCard objectiveCard = objDeck.getCard();
                System.out.println(objectiveCard);
            } catch (DeckException deckExc){
                deckExc.printStackTrace(System.err);
                break;
            }
        }
    }

    @Test
    void getFirstRevealed() {
        System.out.println(objDeck.getFirstRevealed());
    }

    @Test
    void getSecondRevealed() {
        System.out.println(objDeck.getSecondRevealed());
    }



}