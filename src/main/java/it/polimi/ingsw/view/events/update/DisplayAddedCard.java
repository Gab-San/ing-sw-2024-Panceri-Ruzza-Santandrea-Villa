package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.tui.TUI;

import java.util.List;

/**
 * This class represents an event triggered by a card being added to the player's hand
 */
public class DisplayAddedCard extends DisplayPlayerEvent {

    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayAddedCard(String nickname, boolean isLocalPlayer) {
        super(nickname, isLocalPlayer);
    }

    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer){
            tui.showNotification("You have drawn a card");
            return;
        }
        tui.showNotification(nickname + " has drawn a card");
    }
}
