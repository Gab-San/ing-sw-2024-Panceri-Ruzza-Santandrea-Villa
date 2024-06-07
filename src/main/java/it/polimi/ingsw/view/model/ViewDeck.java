package it.polimi.ingsw.view.model;

import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.events.update.DisplayDeckUpdate;
import it.polimi.ingsw.view.model.cards.ViewCard;

public class ViewDeck<C extends ViewCard>{
    private final char deckId;
    private C topCard;
    private C firstRevealed;
    private C secondRevealed;
    private final View view;

    public ViewDeck(char deckId, View view){
        this.deckId = deckId;
        this.view = view;
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
        // Checks are inside the event
        notifyView(SceneID.getBoardSceneID(), new DisplayDeckUpdate(deckId, topCard, 0));
    }

    public synchronized C getFirstRevealed() {
        return firstRevealed;
    }
    public synchronized void setFirstRevealed(C card) {
        if(card != null) card.turnFaceUp();
        this.firstRevealed = card;
        // Checks inside event
        notifyView(SceneID.getBoardSceneID(), new DisplayDeckUpdate(deckId, firstRevealed, 1));

    }

    public synchronized C getSecondRevealed() {
        return secondRevealed;
    }
    public synchronized void setSecondRevealed(C card) {
        if(card != null) card.turnFaceUp();
        this.secondRevealed = card;
        // Checks inside event
        notifyView(SceneID.getBoardSceneID(), new DisplayDeckUpdate(deckId, secondRevealed, 2));
    }

    public static String getDeckName(char deck){
        return switch (deck) {
            case ViewBoard.RESOURCE_DECK -> "Resource deck";
            case ViewBoard.GOLD_DECK -> "Gold deck";
            case ViewBoard.OBJECTIVE_DECK -> "Objective deck";
            default -> "";
        };
    }

    public synchronized void notifyView(SceneID scene, DisplayEvent event){
        view.update(scene, event);
    }
}
