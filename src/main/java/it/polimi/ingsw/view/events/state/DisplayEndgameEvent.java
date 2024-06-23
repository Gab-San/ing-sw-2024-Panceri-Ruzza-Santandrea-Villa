package it.polimi.ingsw.view.events.state;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.events.GUIEvent;
import it.polimi.ingsw.view.events.TUIEvent;
import it.polimi.ingsw.view.gui.GameGUI;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This class handles endgame notification event.
 */
public class DisplayEndgameEvent implements TUIEvent, GUIEvent {
    private final String nickname;
    private final int score;
    public DisplayEndgameEvent(){
        nickname = null;
        score = 0;
    }

    public DisplayEndgameEvent(String nickname, int score){
        this.nickname = nickname;
        this.score = score;
    }
    @Override
    public void displayEvent(TUI tui) {
        displayNotification(tui);
    }

    @Override
    public void displayEvent(GameGUI gui) {
        displayNotification(gui);
    }

    private void displayNotification(View view){
        if(nickname == null) {
            view.showNotification("Endgame has been reached!");
            return;
        }
        view.showNotification("Endgame has been reached because "
                + nickname + " has reached " + score + " (>20) points!");
    }
}
