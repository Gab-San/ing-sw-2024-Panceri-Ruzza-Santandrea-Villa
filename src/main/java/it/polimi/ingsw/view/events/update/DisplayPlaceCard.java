package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;
import it.polimi.ingsw.view.tui.TUI;

import java.util.Map;

//DOCS add docs
public class DisplayPlaceCard extends DisplayPlayerEvent {
    //TODO decide if to pass only card
    private final Map<GamePoint, ViewPlaceableCard> cardMatrix;
    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayPlaceCard(String nickname, boolean isLocalPlayer,
                               Map<GamePoint, ViewPlaceableCard> cardMatrix) {
        super(nickname, isLocalPlayer);
        this.cardMatrix = cardMatrix;
    }

    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer){
            tui.showNotification("You placed a card");
            return;
        }
        tui.showNotification(nickname + " placed a card");
    }
}
