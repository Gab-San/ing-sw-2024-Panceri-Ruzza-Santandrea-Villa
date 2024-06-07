package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.tui.TUI;

import java.util.List;

public class DisplayFreeCorners extends DisplayPlayerEvent {
    private final List<ViewCorner> freeCorners;
    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayFreeCorners(String nickname, boolean isLocalPlayer, List<ViewCorner> freeCorners) {
        super(nickname, isLocalPlayer);
        this.freeCorners = freeCorners;
    }

    @Override
    public void displayEvent(GUI gui) {

    }

    @Override
    public void displayEvent(TUI tui) {
        // Check if flash worsen
        tui.refreshScene();
    }
}
