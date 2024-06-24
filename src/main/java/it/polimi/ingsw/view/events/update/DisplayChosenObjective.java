package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents an event triggered by a user when choosing the objective card.
 */
public class DisplayChosenObjective extends DisplayPlayerEvent {

    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayChosenObjective(String nickname, boolean isLocalPlayer) {
        super(nickname, isLocalPlayer);
    }


    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer){
            tui.showNotification("You have chosen your objective");
            return;
        }
        tui.showNotification(nickname + " has chosen their objective");
    }
}
