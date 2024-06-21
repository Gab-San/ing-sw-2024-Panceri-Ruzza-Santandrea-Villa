package it.polimi.ingsw.view.events;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.SceneManager;
import it.polimi.ingsw.view.gui.GameGUI;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUI_Scene;
import it.polimi.ingsw.view.tui.scenes.PrintEndgameUI;

/**
 * This class represents an event triggered by reaching the endgame. It displays the endgame screen.
 */
public class GotoEndgameEvent implements TUIEvent, GUIEvent{
    private final ViewBoard board;
    boolean atLeast2Players;
    public GotoEndgameEvent(ViewBoard board){
        this.board = board;
        atLeast2Players = board.getOpponents().stream().anyMatch(ViewOpponentHand::isConnected);
    }
    @Override
    public void displayEvent(GameGUI gui) {
        gui.updatePhase(GamePhase.SHOWWIN);
    }

    @Override
    public void displayEvent(TUI tui) {
        TUI_Scene endgameUI = new PrintEndgameUI(board, atLeast2Players);
        tui.setBacklogs(endgameUI);
        SceneManager.getInstance().loadScene(SceneID.getEndgameSceneID(), endgameUI);
        SceneManager.getInstance().setScene(SceneID.getEndgameSceneID());
    }
}
