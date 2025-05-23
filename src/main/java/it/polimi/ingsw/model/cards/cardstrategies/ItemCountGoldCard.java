package it.polimi.ingsw.model.cards.cardstrategies;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.GameResource;

/**
 * This class implements the gold card strategy that gives points for each
 * visible resource of the specified type on the player's play area.
 */
public class ItemCountGoldCard implements GoldCardStrategy{
    private final GameResource item;

    /**
     * Constructs an item count gold strategy for the specified item.
     * @param item item to count
     */
    public ItemCountGoldCard(GameResource item){
        this.item = item;
    }

    /**
     * Returns the score multiplier of this gold card.
     *<p>
     *     The score multiplier is calculated on how many visible resources
     *     equals to item are on the play area.
     *</p>
     * @param playArea current player's play area
     * @param card currently played card
     * @return the score multiplier of this gold card
     */
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
