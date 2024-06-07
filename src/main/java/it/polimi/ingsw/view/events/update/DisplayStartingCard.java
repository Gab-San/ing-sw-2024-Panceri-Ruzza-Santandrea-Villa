package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.cards.ViewStartCard;
import it.polimi.ingsw.view.tui.TUI;
//DOCS add docs
public class DisplayStartingCard extends DisplayPlayerEvent {
    private final ViewStartCard startingCard;
    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayStartingCard(String nickname, boolean isLocalPlayer, ViewStartCard startingCard) {
        super(nickname, isLocalPlayer);
        this.startingCard = startingCard;
    }

    @Override
    public void displayEvent(GUI gui) {

    }

    @Override
    public void displayEvent(TUI tui) {
        if(startingCard == null){
            if(isLocalPlayer){
                tui.showNotification("You have placed your starting card");
                return;
            }
            tui.showNotification(nickname + " has placed their starting card");
            return;
        }
        // Here startingCard != null
        if(isLocalPlayer){
            tui.showNotification("You have received your starting card");
            return;
        }
        tui.showNotification(nickname + " has received their starting card");
    }
}
