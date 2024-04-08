package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.nio.file.Path;

public class GoldCardFactory extends CardFactory {

    public GoldCardFactory(Path idFile, Path importFile) {
        super(idFile, importFile);
    }

    @Override
    public PlayCard addCardToDeck() throws Exception {
        String cardId = remainingCards.remove(getRandomCardID());
        // TODO to implement when JSON is functional
        return instantiateCard();
    }

    @Override
    protected PlayCard instantiateCard() {
        return null;
    }

    @Override
    protected void importFromJSON() {

    }
}
