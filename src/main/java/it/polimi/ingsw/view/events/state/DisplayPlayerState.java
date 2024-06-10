package it.polimi.ingsw.view.events.state;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents an event triggered by a player state update.
 */
public class DisplayPlayerState extends DisplayPlayerEvent {
    private final boolean isConnected;
    private final int turn;
    private final PlayerColor color;

    /**
     * Constructs player state event for remote/opponent player.
     * @param nickname opponent's nickname
     * @param isConnected opponent's connection status
     * @param turn opponent's turn
     * @param color opponent's color
     */
    public DisplayPlayerState(String nickname,
                              boolean isConnected, int turn, PlayerColor color) {
        super(nickname, false);
        this.isConnected = isConnected;
        this.turn = turn;
        this.color = color;
    }

    /**
     * Constructs player state event for local player.
     * @param nickname local player's nickname
     * @param turn local player's turn
     * @param color local player's color
     */
    public DisplayPlayerState(String nickname, int turn, PlayerColor color){
        super(nickname, true);
        this.isConnected = true;
        this.turn = turn;
        this.color = color;
    }

    @Override
    public void displayEvent(TUI tui) {
        if(isLocalPlayer) {
            tui.showNotification("Your turn and color were set.");
            return;
        }
        tui.showNotification(nickname + "'s hand and state was set.");
    }
}
