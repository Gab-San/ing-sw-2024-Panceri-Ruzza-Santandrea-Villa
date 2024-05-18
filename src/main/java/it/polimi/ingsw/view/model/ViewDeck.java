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

    public synchronized boolean isEmpty() {
        return topCard == null;
    }

    public synchronized C getTopCard() {
        return topCard;
    }
    public synchronized void setTopCard(C card) {
        if(card != null) card.turnFaceDown();
        this.topCard = card;
    }

    public synchronized C getFirstRevealed() {
        return firstRevealed;
    }
    public synchronized void setFirstRevealed(C card) {
        if(card != null) card.turnFaceUp();
        this.firstRevealed = card;
    }

    public synchronized C getSecondRevealed() {
        return secondRevealed;
    }
    public synchronized void setSecondRevealed(C card) {
        if(card != null) card.turnFaceUp();
        this.secondRevealed = card;
    }
}
