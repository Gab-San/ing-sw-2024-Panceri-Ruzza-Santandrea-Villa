package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

import java.util.Map;

//DOCS add docs
public class DisplayVisibleResources extends DisplayPlayerEvent {
    private final Map<GameResource, Integer> visibleResources;
    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayVisibleResources(String nickname, boolean isLocalPlayer,
                                   Map<GameResource, Integer> visibleResources) {
        super(nickname, isLocalPlayer);
        this.visibleResources = visibleResources;
    }

    @Override
    public void displayEvent(GUI gui) {

    }

    @Override
    public void displayEvent(TUI tui) {
        // Might flash a bit
        tui.refreshScene();
    }
}
