package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This event handles player's turn assignment event.
 */
public class DisplayPlayerTurn extends DisplayPlayerEvent {
    private final int playerTurn;
    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayPlayerTurn(String nickname, boolean isLocalPlayer, int playerTurn) {
        super(nickname, isLocalPlayer);
        this.playerTurn = playerTurn;
    }

    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer){
            tui.showNotification("Your turn was set to " + playerTurn);
            return;
        }
        tui.showNotification(nickname + "'s turn was set to " + playerTurn);
    }
}
