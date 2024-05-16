package it.polimi.ingsw.view.model;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.model.cards.ViewCard;

public class ViewDeck<C extends ViewCard>{
    private boolean isEmpty;
    private C topCard;
    private C firstRevealed;
    private C secondRevealed;

    public ViewDeck(){
        isEmpty = false;
        firstRevealed = null;
        secondRevealed = null;
    }

    public boolean isEmpty() {
        return topCard == null;
    }

    public C getTopCard() {
        return topCard;
    }
    public void setTopCard(C topCard) {
        this.topCard = topCard;
    }

    public C getFirstRevealed() {
        return firstRevealed;
    }
    public void setFirstRevealed(C firstRevealed) {
        this.firstRevealed = firstRevealed;
    }

    public C getSecondRevealed() {
        return secondRevealed;
    }
    public void setSecondRevealed(C secondRevealed) {
        this.secondRevealed = secondRevealed;
    }
}
