package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.cards.ViewStartCard;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents an event triggered by a starting card being given to the player.
 */
public class DisplayStartingCard extends DisplayPlayerEvent {
    private final ViewStartCard startingCard;
    private final boolean wasStartCardNull;
    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     * @param wasStartCardNull true if old value of starting card was null
     * @param startingCard the starting card value that was set on player hand
     */
    public DisplayStartingCard(String nickname, boolean isLocalPlayer, boolean wasStartCardNull, ViewStartCard startingCard) {
        super(nickname, isLocalPlayer);
        this.startingCard = startingCard;
        this.wasStartCardNull = wasStartCardNull;
    }

    @Override
    public void displayEvent(TUI tui) {
        if(wasStartCardNull && startingCard == null){
            //no update if start card does not change
            return;
        }
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
