package it.polimi.ingsw.view.events.state;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents an event triggered by a hand state update.
 */
public class DisplayHandState extends DisplayPlayerEvent {

    /**
     * Constructs hand state event.
     *
     * @param nickname player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayHandState(String nickname, boolean isLocalPlayer) {
        super(nickname, isLocalPlayer);
    }


    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer){
            tui.showNotification("Your hand was set");
            return;
        }
        tui.showNotification(nickname + "'s hand was set");
    }
}
