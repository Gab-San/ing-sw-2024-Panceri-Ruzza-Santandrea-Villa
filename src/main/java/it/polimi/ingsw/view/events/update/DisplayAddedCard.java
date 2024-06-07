package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.tui.TUI;

import java.util.List;

//DOCS [Gamba] add docs
public class DisplayAddedCard extends DisplayPlayerEvent {
    //TODO: decide whether to pass all cards or only added
    private final List<ViewPlayCard> playCardList;
    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayAddedCard(String nickname, boolean isLocalPlayer, List<ViewPlayCard> playCardList) {
        super(nickname, isLocalPlayer);
        this.playCardList = playCardList;
    }

    @Override
    public void displayEvent(GUI gui) {

    }

    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer){
            tui.showNotification("You have drawn a card");
            return;
        }
        tui.showNotification(nickname + " has drawn a card");
    }
}
