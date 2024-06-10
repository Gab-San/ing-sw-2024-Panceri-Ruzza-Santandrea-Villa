package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.tui.TUI;

import java.util.List;

//DOCS [Gamba] add docs
public class DisplayRemoveCards extends DisplayPlayerEvent {
    private final List<ViewPlayCard> playCards;
    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayRemoveCards(String nickname, boolean isLocalPlayer, List<ViewPlayCard> playCards) {
        super(nickname, isLocalPlayer);
        this.playCards = playCards;
    }



    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer){
            tui.showNotification("You have used a card in your hand");
            return;
        }
        tui.showNotification(nickname + " has drawn a card");
    }
}
