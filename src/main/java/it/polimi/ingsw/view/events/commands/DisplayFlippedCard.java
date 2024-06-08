package it.polimi.ingsw.view.events.commands;

import it.polimi.ingsw.view.events.GUIEvent;
import it.polimi.ingsw.view.events.TUIEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.tui.TUI;
//DOCS add docs
public class DisplayFlippedCard implements TUIEvent, GUIEvent {
    private final ViewPlayCard flippedCard;
    public DisplayFlippedCard(ViewPlayCard flippedCard){
        this.flippedCard = flippedCard;
    }

    @Override
    public void displayEvent(GUI gui) {

    }

    @Override
    public void displayEvent(TUI tui) {
        tui.showNotification("Flipped card " + flippedCard.getCardID());
    }
}
