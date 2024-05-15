package it.polimi.ingsw.view.model;

import it.polimi.ingsw.view.model.cards.ViewCard;

public class ViewDeck<C extends ViewCard>{
    private boolean isEmpty;
    private C firstRevealed;
    private C secondRevealed;

    public ViewDeck(){
        isEmpty = false;
        firstRevealed = null;
        secondRevealed = null;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
    public void setEmpty(boolean empty) {
        isEmpty = empty;
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
