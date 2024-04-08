package it.polimi.ingsw.model.deck.cardfactory;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.nio.file.Path;

public class ResourceCardFactory extends CardFactory {

    public ResourceCardFactory(Path idFile, Path importFile) {
        super(idFile, importFile);
    }

    @Override
    public PlayCard addCardToDeck() {
        String cardId = remainingCards.remove(getRandomCardID());
        //TODO complete when JSON is functional
        return instantiateCard();
    }

    @Override
    protected PlayCard instantiateCard() {
        ResourceCard emptyCard = new ResourceCard();
        return emptyCard;
    }

    @Override
    protected void importFromJSON() {

    }
}
