package it.polimi.ingsw.view.model;

import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.events.update.DisplayDeckUpdate;
import it.polimi.ingsw.view.model.cards.ViewCard;

/**
 * View lightweight representation of the model's Decks
 * @param <C> the card type of this deck
 */
public class ViewDeck<C extends ViewCard>{
    private final char deckId;
    private C topCard;
    private C firstRevealed;
    private C secondRevealed;
    private final View view;

    /**
     * Constructs the deck
     * @param deckId this deck letter identifier (e.g. R for Resource deck)
     * @param view the View this deck should notify events to.
     */
    public ViewDeck(char deckId, View view){
        this.deckId = deckId;
        this.view = view;
        topCard = null;
        firstRevealed = null;
        secondRevealed = null;
    }

    /**
     * @return true if the top card of this deck is empty
     */
    public synchronized boolean isEmpty() {
        return topCard == null;
    }

    /**
     * @return the top card of this deck
     */
    public synchronized C getTopCard() {
        return topCard;
    }

    /**
     * Set the top card of the deck. <br>
     * Always turns the card face-down. <br>
     * Notifies the view of the Deck update
     * @param card the top card
     */
    public synchronized void setTopCard(C card) {
        if(card != null) card.turnFaceDown();
        this.topCard = card;
        // Checks are inside the event
        notifyView(SceneID.getBoardSceneID(), new DisplayDeckUpdate(deckId, topCard, 0));
    }

    /**
     * @return the first revealed card of this deck
     */
    public synchronized C getFirstRevealed() {
        return firstRevealed;
    }

    /**
     * Set the first revealed card of the deck. <br>
     * Always turns the card face-up. <br>
     * Notifies the view of the Deck update
     * @param card the first revealed card
     */
    public synchronized void setFirstRevealed(C card) {
        if(card != null) card.turnFaceUp();
        this.firstRevealed = card;
        // Checks inside event
        notifyView(SceneID.getBoardSceneID(), new DisplayDeckUpdate(deckId, firstRevealed, 1));

    }
    /**
     * @return the second revealed card of this deck
     */
    public synchronized C getSecondRevealed() {
        return secondRevealed;
    }
    /**
     * Set the second revealed card of the deck. <br>
     * Always turns the card face-up. <br>
     * Notifies the view of the Deck update
     * @param card the second revealed card
     */
    public synchronized void setSecondRevealed(C card) {
        if(card != null) card.turnFaceUp();
        this.secondRevealed = card;
        // Checks inside event
        notifyView(SceneID.getBoardSceneID(), new DisplayDeckUpdate(deckId, secondRevealed, 2));
    }

    /**
     * @param deck the deck initial
     * @return the full name associated with the given initial (or an empty string if none match)
     */
    public static String getDeckName(char deck){
        return switch (deck) {
            case ViewBoard.RESOURCE_DECK -> "Resource deck";
            case ViewBoard.GOLD_DECK -> "Gold deck";
            case ViewBoard.OBJECTIVE_DECK -> "Objective deck";
            default -> "";
        };
    }

    /**
     * Notifies the view of an event
     * @param scene the scene concerned by the event
     * @param event the event to notify
     */
    public synchronized void notifyView(SceneID scene, DisplayEvent event){
        view.update(scene, event);
    }
}
