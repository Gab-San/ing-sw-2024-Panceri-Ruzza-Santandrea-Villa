package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents an event triggered when a card is removed from a player's hand.
 */
public class DisplayRemoveCards extends DisplayPlayerEvent {

    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayRemoveCards(String nickname, boolean isLocalPlayer) {
        super(nickname, isLocalPlayer);
    }



    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer){
            tui.showNotification("You have used a card in your hand");
            return;
        }
        tui.showNotification(nickname + " has drawn a card");
    }
}
