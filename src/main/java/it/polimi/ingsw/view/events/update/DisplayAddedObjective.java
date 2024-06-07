package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.tui.TUI;

import java.util.List;

//DOCS ADD DOCS
public class DisplayAddedObjective extends DisplayPlayerEvent {
    private final List<ViewObjectiveCard> objectiveCards;
    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayAddedObjective(String nickname, boolean isLocalPlayer, List<ViewObjectiveCard> objectiveCards) {
        super(nickname, isLocalPlayer);
        this.objectiveCards = objectiveCards;
    }

    @Override
    public void displayEvent(GUI gui) {

    }

    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer){
            tui.showNotification("You have received an objective");
            return;
        }
        tui.showNotification(nickname + " has received an objective");
    }
}
