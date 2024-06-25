package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.exceptions.DeckException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GoldCardFactoryTest {
    static GoldCardFactory gFactory;

    @BeforeAll
    public static void setup(){
        try {
            gFactory = new GoldCardFactory();
        } catch (IllegalStateException deckExc){
            deckExc.printStackTrace(System.err);
        }
    }

    @Test
    @DisplayName("Get One Card")
    void addCardToDeck() {
        try {
            GoldCard goldCard = (GoldCard) gFactory.addCardToDeck();
            System.out.println(goldCard);
        } catch (DeckException deckExc){
            deckExc.printStackTrace(System.err);
        }
    }

    @Test
    @DisplayName("Finish Deck")
    void addCardToDeckTillEnd(){
        while(true){
            try {
                GoldCard goldCard = (GoldCard) gFactory.addCardToDeck();
                System.out.println(goldCard);
            } catch (DeckException deckExc){
                deckExc.printStackTrace(System.err);
                break;
            }
        }
    }

    @Test
    void tryToImportFromWrongFile(){
        GoldCardFactory goldCardFactory = new GoldCardFactory("TestFile");

        assertThrows(
            DeckException.class,
            goldCardFactory::addCardToDeck
        );
    }
}