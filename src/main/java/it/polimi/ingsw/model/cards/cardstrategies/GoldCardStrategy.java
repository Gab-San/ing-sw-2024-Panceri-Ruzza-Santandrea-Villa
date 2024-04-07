package it.polimi.ingsw.model.cards.cardstrategies;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.GoldCard;

public interface GoldCardStrategy {
    //TODO write documentation for the patterns
    public int calculateSolves(PlayArea pA, GoldCard card);
}
