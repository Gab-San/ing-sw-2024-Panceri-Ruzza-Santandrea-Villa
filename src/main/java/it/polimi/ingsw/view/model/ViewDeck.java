package it.polimi.ingsw.view.model;

import it.polimi.ingsw.view.model.cards.ViewCard;

public class ViewDeck<C extends ViewCard>{
    private char deckType;
    private C topCard;
    private C firstRevealed;
    private C secondRevealed;

    public ViewDeck(char deckType){
        this.deckType = deckType;
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
        if(card == null){
            view.notifyBoardUpdate(getDeckName(deckType) + " is now empty! Only the 2 revealed cards remain.");
        } else {
            view.notifyBoardUpdate("The top card of " + getDeckName(deckType) + " was replaced");
        }
    }

    public synchronized C getFirstRevealed() {
        return firstRevealed;
    }
    public synchronized void setFirstRevealed(C card) {
        if(card != null) card.turnFaceUp();
        this.firstRevealed = card;
        if(card == null){
            view.notifyBoardUpdate("First revealed card of " + getDeckName(deckType) + " was drawn. Deck is empty so no card has replaced it.");
        } else{
            view.notifyBoardUpdate("First revealed card of " + getDeckName(deckType) + " was revealed");
        }
    }

    public synchronized C getSecondRevealed() {
        return secondRevealed;
    }
    public synchronized void setSecondRevealed(C card) {
        if(card != null) card.turnFaceUp();
        this.secondRevealed = card;
        if(card == null){
            view.notifyBoardUpdate("Second revealed card of " + getDeckName(deckType) + " was drawn. Deck is empty so no card has replaced it.");
        } else {
            view.notifyBoardUpdate("Second revealed card of " + getDeckName(deckType) + " was revealed");
        }
    }

    private String getDeckName(char deck){
        return switch (deck) {
            case ViewBoard.RESOURCE_DECK -> "Resource deck";
            case ViewBoard.GOLD_DECK -> "Gold deck";
            case ViewBoard.OBJECTIVE_DECK -> "Objective deck";
            default -> "";
        };
    }
}
