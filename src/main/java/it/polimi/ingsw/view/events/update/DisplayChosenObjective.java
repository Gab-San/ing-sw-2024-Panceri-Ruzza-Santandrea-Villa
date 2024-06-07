package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;
//DOCS add docs
public class DisplayChosenObjective extends DisplayPlayerEvent {
    private final String choiceID;
    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayChosenObjective(String nickname, boolean isLocalPlayer, String choiceID) {
        super(nickname, isLocalPlayer);
        this.choiceID = choiceID;
    }

    @Override
    public void displayEvent(GUI gui) {

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
