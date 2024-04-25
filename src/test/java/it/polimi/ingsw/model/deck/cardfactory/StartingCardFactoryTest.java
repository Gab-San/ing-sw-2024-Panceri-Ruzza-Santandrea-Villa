package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.cards.StartingCard;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StartingCardFactoryTest {
    final StartingCardFactory sFactory = new StartingCardFactory();

    @Test
    void addCardToDeck() {
        boolean isEmpty = false;
        while (!isEmpty){
            try {
                StartingCard startCard = sFactory.addCardToDeck();
                System.out.println(startCard);
            } catch (Exception exception) {
//                System.err.println(exception.getMessage());
                isEmpty = true;
            }
        }
    }

}