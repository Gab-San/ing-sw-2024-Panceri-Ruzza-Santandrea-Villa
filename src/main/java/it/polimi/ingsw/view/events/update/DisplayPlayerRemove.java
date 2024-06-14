package it.polimi.ingsw.view.events.update;

import it.polimi.ingsw.view.Scene;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.SceneManager;
import it.polimi.ingsw.view.events.DisplayPlayerEvent;
import it.polimi.ingsw.view.events.GUIEvent;
import it.polimi.ingsw.view.gui.GameGUI;
import it.polimi.ingsw.view.tui.TUI;

/**
 * This event handles player removal update.
 */
public class DisplayPlayerRemove extends DisplayPlayerEvent implements GUIEvent {
    /**
     * Constructs player removal event.
     *
     * @param nickname player's nickname who caused event to be triggered
     */
    public DisplayPlayerRemove(String nickname) {
        super(nickname, false);
    }


    @Override
    public void displayEvent(TUI tui) {
        Scene currentScene = SceneManager.getInstance().getCurrentScene();
        Scene opponentScene = SceneManager.getInstance().getScene(SceneID.getOpponentAreaSceneID(nickname));
        if(currentScene.equals(opponentScene)){
            SceneManager.getInstance().setScene(SceneID.getMyAreaSceneID());
        }
        tui.showNotification(nickname + " has been removed.");
        SceneManager.getInstance().remove(SceneID.getOpponentAreaSceneID(nickname));
    }

    @Override
    public void displayEvent(GameGUI gui) {
        gui.removePlayerScene(nickname);
    }
}
