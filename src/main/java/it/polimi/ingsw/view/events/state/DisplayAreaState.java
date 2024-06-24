package it.polimi.ingsw.view.events.state;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents an event triggered by a state update of one of the player areas
 */
public class DisplayAreaState extends DisplayPlayerEvent {

    /**
     * Constructs play area state event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayAreaState(String nickname, boolean isLocalPlayer) {
        super(nickname, isLocalPlayer);
    }


    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer){
            tui.showNotification("Your playArea was initialised");
            return;
        }
        tui.showNotification(nickname + "'s playArea was initialised");
    }
}
