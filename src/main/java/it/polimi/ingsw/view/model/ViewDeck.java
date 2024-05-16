package it.polimi.ingsw.view.model;

import it.polimi.ingsw.view.model.cards.ViewCard;

public class ViewDeck<C extends ViewCard>{
    private C topCard;
    private C firstRevealed;
    private C secondRevealed;

    public ViewDeck(){
        topCard = null;
        firstRevealed = null;
        secondRevealed = null;
    }

    public boolean isEmpty() {
        return topCard == null;
    }

    public C getTopCard() {
        return topCard;
    }
    public void setTopCard(C card) {
        if(card != null) card.turnFaceDown();
        this.topCard = card;
    }

    public C getFirstRevealed() {
        return firstRevealed;
    }
    public void setFirstRevealed(C card) {
        if(card != null) card.turnFaceUp();
        this.firstRevealed = card;
    }

    public C getSecondRevealed() {
        return secondRevealed;
    }
    public void setSecondRevealed(C card) {
        if(card != null) card.turnFaceUp();
        this.secondRevealed = card;
    }
}
