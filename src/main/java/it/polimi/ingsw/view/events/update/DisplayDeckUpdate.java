package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.GUIEvent;
import it.polimi.ingsw.view.events.TUIEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.tui.TUI;

import static it.polimi.ingsw.view.model.ViewDeck.getDeckName;
//DOCS [Gamba] add docs
public class DisplayDeckUpdate implements TUIEvent, GUIEvent {
    private final char deckId;
    private final ViewCard updatedCard;
    private final int cardPosition;

    public DisplayDeckUpdate(char deckId, ViewCard updatedCard, int cardPosition) {
        this.deckId = deckId;
        this.updatedCard = updatedCard;
        this.cardPosition = cardPosition;
    }


    @Override
    public void displayEvent(GUI gui) {

    }

    @Override
    public void displayEvent(TUI tui) {
        if(updatedCard == null){
            switch (cardPosition){
                case 0:
                    tui.showNotification(getDeckName(deckId) + " is now empty! Only the 2 revealed cards remain.");
                    return;
                case 1:
                    tui.showNotification("First revealed card of " + getDeckName(deckId) +
                            " is empty.");
                    return;
                case 2:
                    tui.showNotification("Second revealed card of " + getDeckName(deckId) +
                            " is empty.");
                    return;
            }
        }

        // Here updated card is not null and must notify view
        switch (cardPosition){
            case 0:
                tui.showNotification("The top card of " + getDeckName(deckId) + " was replaced");
                return;
            case 1:
                tui.showNotification("First revealed card of " + getDeckName(deckId) + " was revealed");
                return;
            case 2:
                tui.showNotification("Second revealed card of " + getDeckName(deckId) + " was revealed");
                // No ret needed since last statement
        }
    }
}
