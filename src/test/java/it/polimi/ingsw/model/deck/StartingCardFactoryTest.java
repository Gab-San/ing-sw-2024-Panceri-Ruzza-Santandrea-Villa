package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.cards.StartingCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StartingCardFactoryTest {
    static StartingCardDeck sFactory;

    @BeforeAll
    static void setup(){
        try {
            sFactory = new StartingCardDeck();
        } catch (IllegalStateException deckExc){
            deckExc.printStackTrace(System.err);
        }
    }



    @Test
    void addCardToDeck() {
        boolean isEmpty = false;
        while (!isEmpty){
            try {
                StartingCard startCard = sFactory.getCard();
                System.out.println(startCard);
            } catch (Exception exception) {
//                System.err.println(exception.getMessage());
                isEmpty = true;
            }
        }
    }

}