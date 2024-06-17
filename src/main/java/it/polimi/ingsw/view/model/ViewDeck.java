package it.polimi.ingsw.view.model;

import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.events.update.DisplayDeckUpdate;
import it.polimi.ingsw.view.gui.CardListener;
import it.polimi.ingsw.view.gui.DeckListener;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

import javax.swing.*;
import java.awt.*;

/**
 * View lightweight representation of the model's Decks
 * @param <C> the card type of this deck
 */
public class ViewDeck<C extends ViewCard> extends JComponent implements CardListener {
    private final char deckId;
    private C topCard;
    private C firstRevealed;
    private C secondRevealed;
    private final View view;
    private CardListener cardListener;
    private DeckListener deckListener;

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
        setupView();
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
        removeCard(topCard);
        this.topCard = card;
        if(topCard != null) {
            topCard.setCardListener(this);
            topCard.disableComponent();
        }
        // Checks are inside the event
        notifyView(SceneID.getBoardSceneID(), new DisplayDeckUpdate(deckId, topCard, 0));
        // When a card is set than it always changes. No need to check if it's the same
//        firePropertyChange(ChangeNotifications.ADD_CARD_DECK, null, new CardDeckInfo(deckId, card, 0));
        displayChange(topCard, 0);
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
        removeCard(firstRevealed);
        this.firstRevealed = card;
        if(firstRevealed != null) {
            firstRevealed.setCardListener(this);
            firstRevealed.disableComponent();
        }
        // Checks inside event
        notifyView(SceneID.getBoardSceneID(), new DisplayDeckUpdate(deckId, firstRevealed, 1));
//        firePropertyChange(ChangeNotifications.ADD_CARD_DECK, null, new CardDeckInfo(deckId, card, 1));
        displayChange(firstRevealed, 1);
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
        removeCard(secondRevealed);
        this.secondRevealed = card;
        if(secondRevealed != null) {
            secondRevealed.setCardListener(this);
            secondRevealed.disableComponent();
        }
        // Checks inside event
        notifyView(SceneID.getBoardSceneID(), new DisplayDeckUpdate(deckId, secondRevealed, 2));
        displayChange(secondRevealed, 2);
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

//region GUI METHODS
    private void setupView() {
        setLayout(new GridLayout(3,1, 0, 10));
        setOpaque(false);
    }

    private void displayChange(ViewCard card, int cardPosition){
        //FIXME use a gridbag layout
        SwingUtilities.invokeLater(
                () ->{
                    if(card != null) {
                        add(card, cardPosition);
                    }
                    revalidate();
                    repaint();
                }
        );
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(ViewCard.getScaledWidth(), 3*(ViewCard.getScaledHeight() + 10));
    }

    private void removeCard(ViewCard removedCard){
        if(removedCard != null){
            removedCard.setCardListener(null);
            remove(removedCard);
        }
    }

    public void setCardListener(CardListener cardListener) {
        this.cardListener = cardListener;
    }
    public void setDeckListener(DeckListener deckListener){
        this.deckListener = deckListener;
    }

    @Override
    public void setSelectedCard(ViewPlaceableCard card) {
        cardListener.setSelectedCard(card);
        int cardPosition = -1;
        if(card.equals(topCard)){
            cardPosition = 0;
        } else if(card.equals(firstRevealed)){
            cardPosition = 1;
        } else if(card.equals(secondRevealed)){
            cardPosition = 2;
        }

        if(cardPosition == -1){
            throw new IllegalStateException("SELECTED CARD NOT IN DECK");
        }

        deckListener.setSelectedPosition(deckId, cardPosition);
    }
    //endregion
}
