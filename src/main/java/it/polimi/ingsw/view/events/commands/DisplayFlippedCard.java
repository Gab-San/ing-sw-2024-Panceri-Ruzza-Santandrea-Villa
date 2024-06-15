package it.polimi.ingsw.view.events.commands;

import it.polimi.ingsw.view.events.GUIEvent;
import it.polimi.ingsw.view.events.TUIEvent;
import it.polimi.ingsw.view.gui.GameGUI;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents a flip card event. This event is triggered by an interaction
 * of the user with one of his owned cards.
 */
public class DisplayFlippedCard implements TUIEvent{
    private final ViewPlaceableCard flippedCard;

    /**
     * Constructs flipped card event.
     * @param flippedCard flipped card ref
     */
    public DisplayFlippedCard(ViewPlaceableCard flippedCard){
        this.flippedCard = flippedCard;
    }

    @Override
    public void displayEvent(TUI tui) {
        tui.showNotification("Flipped card " + flippedCard.getCardID());
    }
}
