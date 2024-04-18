package it.polimi.ingsw.model.cards.cardstrategies;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.enums.GameResource;

public class ItemCountGoldCard implements GoldCardStrategy{
    private final GameResource item;
    public ItemCountGoldCard(GameResource item){
        this.item = item;
    }
    @Override
    public int calculateSolves(PlayArea playArea, GoldCard card) {
        return playArea.getVisibleResources().get(item);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if(!(other instanceof ItemCountGoldCard)) return false;

        return item == ((ItemCountGoldCard) other).item;
    }
}
