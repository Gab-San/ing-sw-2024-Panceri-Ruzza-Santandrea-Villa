package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.SceneManager;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

//COMMENT FOR "ALE" [Gamba] we could use this event to decide whether there is one last player in game,
// and we could even build the scene at this moment, by passing the board to the Goto event
// I will put it and then comment the code
public class GotoEndgameEvent implements TUIEvent, GUIEvent{
//    private final ViewBoard board;
//    public GotoEndgameEvent(ViewBoard board){
//        this.board = board;
//    }
    @Override
    public void displayEvent(GUI gui) {

    }

    @Override
    public void displayEvent(TUI tui) {
//        TUI_Scene endgameUI = new PrintEndgameUI(board);
//        endgameUI.setNotificationBacklog(tui.notificationBacklog);
//        endgameUI.setChatBacklog(tui.chatBacklog);
//        SceneManager.getInstance().loadScene(SceneID.getEndgameSceneID(), endgameUI);
        SceneManager.getInstance().setScene(SceneID.getEndgameSceneID());
    }
}
