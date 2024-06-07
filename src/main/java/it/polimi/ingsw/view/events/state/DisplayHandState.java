package it.polimi.ingsw.view.events.state;

import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;
import it.polimi.ingsw.view.tui.TUI;

import java.util.List;

/**
 * This class represents an event triggered by a hand state update.
 */
public class DisplayHandState extends DisplayPlayerEvent {
    private final List<ViewPlayCard> playCards;
    private final List<ViewObjectiveCard> objectiveCards;
    private final ViewStartCard startCard;

    /**
     * Constructs hand state event.
     *
     * @param nickname player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     * @param playCards playable cards in player's hand
     * @param objectiveCards objective cards in player's hand
     * @param startCard starting card in player's hand or area
     */
    public DisplayHandState(String nickname, boolean isLocalPlayer, List<ViewPlayCard> playCards,
                               List<ViewObjectiveCard> objectiveCards, ViewStartCard startCard) {
        super(nickname, isLocalPlayer);
        this.playCards = playCards;
        this.objectiveCards = objectiveCards;
        this.startCard = startCard;
    }

    @Override
    public void displayEvent(GUI gui) {

    }

    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer){
            tui.showNotification("Your hand was set");
            return;
        }
        tui.showNotification(nickname + "'s hand was set");
    }
}
