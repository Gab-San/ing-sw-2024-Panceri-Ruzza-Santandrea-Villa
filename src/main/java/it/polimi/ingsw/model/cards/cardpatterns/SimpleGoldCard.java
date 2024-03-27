package it.polimi.ingsw.model.cards.cardpatterns;

public class SimpleGoldCard implements GoldCardStrategy {
    @Override
    public int calculateSolves(PlayArea pA) {
        return 1;
    }
}
