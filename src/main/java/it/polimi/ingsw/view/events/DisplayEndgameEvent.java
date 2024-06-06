package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.tui.TUI;

public class DisplayEndgameEvent implements TUIEvent{
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
        if(nickname == null) {
            tui.showNotification("Endgame has been reached!");
            return;
        }
        tui.showNotification("Endgame has been reached because "
                + nickname + " has reached " + score + " (>20) points!");
    }
}
