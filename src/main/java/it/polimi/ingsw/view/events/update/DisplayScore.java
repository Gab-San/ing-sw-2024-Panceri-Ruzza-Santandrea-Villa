package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.events.TUIEvent;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class represents score update event.
 */
public class DisplayScore implements TUIEvent{
    private final String nickname;
    private final int score;

    /**
     * Constructs score update event.
     *
     * @param nickname player's nickname whose score has changed
     * @param score    updated score
     */
    public DisplayScore(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }

    @Override
    public void displayEvent(TUI tui) {
        tui.showNotification(nickname + "'s score is now " + score);
    }
}
