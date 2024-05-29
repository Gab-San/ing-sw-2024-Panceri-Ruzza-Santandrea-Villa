package it.polimi.ingsw.network.rmi.update.deck;

import it.polimi.ingsw.network.rmi.update.RMIUpdate;
import it.polimi.ingsw.view.ModelUpdater;

abstract public class DeckTypeUpdate extends RMIUpdate {
    protected final char deck;
    protected DeckTypeUpdate(ModelUpdater modelUpdater, char deck) {
        super(modelUpdater);
        this.deck = deck;
    }
}
