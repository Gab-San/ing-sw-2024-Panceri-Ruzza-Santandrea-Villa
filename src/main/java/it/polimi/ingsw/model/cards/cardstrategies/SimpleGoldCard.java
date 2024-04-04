package it.polimi.ingsw.model.cards.cardstrategies;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.GoldCard;

public class SimpleGoldCard implements GoldCardStrategy {
    @Override
    public int calculateSolves(PlayArea pA, GoldCard card) {
        return 1;
    }
}
