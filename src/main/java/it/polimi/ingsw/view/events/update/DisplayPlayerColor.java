package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.getColorFromEnum;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

//DOCS add docs
public class DisplayPlayerColor extends DisplayPlayerEvent {
    private final PlayerColor color;
    /**
     * Constructs player event.
     *
     * @param nickname      player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    public DisplayPlayerColor(String nickname, boolean isLocalPlayer, PlayerColor color) {
        super(nickname, isLocalPlayer);
        this.color = color;
    }

    @Override
    public void displayEvent(GUI gui) {

    }

    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer){
            tui.showNotification("Your color was set to " + getColorFromEnum(color) + color + RESET);
            return;
        }
        tui.showNotification(nickname + "'s color was set to " + getColorFromEnum(color) + color + RESET);
    }
}
