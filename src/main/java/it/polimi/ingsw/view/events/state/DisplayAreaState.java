package it.polimi.ingsw.view.events.state;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.cards.ViewCorner;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;
import it.polimi.ingsw.view.tui.TUI;

import java.util.List;
import java.util.Map;

/**
 * This class represents an event triggered by a state update of one of the player areas
 */
public class DisplayAreaState extends DisplayPlayerEvent {
    private final Map<GamePoint, ViewPlaceableCard> cardMatrix;
    private final Map<GameResource, Integer> visibleResources;
    private final List<ViewCorner> freeCorners;
    /**
     * Constructs play area state event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     * @param cardMatrix    play area cards representation
     * @param visibleResources visible resources in the specified play area
     * @param freeCorners   free corners in the play area
     */
    public DisplayAreaState(String nickname, boolean isLocalPlayer, Map<GamePoint, ViewPlaceableCard> cardMatrix,
                            Map<GameResource, Integer> visibleResources, List<ViewCorner> freeCorners) {
        super(nickname, isLocalPlayer);
        this.cardMatrix = cardMatrix;
        this.visibleResources = visibleResources;
        this.freeCorners = freeCorners;
    }

    @Override
    public void displayEvent(GUI gui) {

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
