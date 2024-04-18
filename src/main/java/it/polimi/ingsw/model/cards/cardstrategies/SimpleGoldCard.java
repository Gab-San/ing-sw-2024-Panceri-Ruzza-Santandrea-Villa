package it.polimi.ingsw.model.cards.cardstrategies;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.GoldCard;

public class SimpleGoldCard implements GoldCardStrategy {
    @Override
    public int calculateSolves(PlayArea pA, GoldCard card) {
        return 1;
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        // A simple gold card is identified only by its class
        return other instanceof SimpleGoldCard;
    }
}
