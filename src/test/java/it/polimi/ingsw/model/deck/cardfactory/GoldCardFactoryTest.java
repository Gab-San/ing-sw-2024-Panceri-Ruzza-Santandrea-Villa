package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GoldCardFactoryTest {
    static GoldCardFactory gFactory;

    @BeforeAll
    public static void setup(){
        try {
            gFactory = new GoldCardFactory("src/main/java/it/polimi/ingsw/model/resources/GoldCard_Id");
        } catch (DeckInstantiationException deckExc){
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
        GoldCardFactory goldCardFactory = null;
        try {
            goldCardFactory = new GoldCardFactory("src/test/java/it/polimi/ingsw/model/deck/TestFile");
        } catch (DeckInstantiationException deckExc){
            deckExc.printStackTrace(System.err);
        }

        try {
            assert goldCardFactory != null;
            GoldCard goldCard = (GoldCard) goldCardFactory.addCardToDeck();
            System.out.println(goldCard);
        } catch (DeckException deckExc){
            deckExc.printStackTrace(System.err);
        }
    }
}