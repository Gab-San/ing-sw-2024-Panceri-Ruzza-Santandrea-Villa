package it.polimi.ingsw.view.gui.scenes.extra.playerpanel;

import it.polimi.ingsw.view.Scene;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.SceneManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ChangeAreaAction extends AbstractAction {
    private ChangeAreaButton oldButton;
    public static final String SCENE_CHANGE = "SCENE_CHANGE_PROPERTY";
    public static final String SCENE_CHANGE_ERROR = "SCENE_CHANGE_ERROR_PROPERTY";
    @Override
    public void actionPerformed(ActionEvent e) {
        assert e.getSource() instanceof ChangeAreaButton;
        ChangeAreaButton areaButton = (ChangeAreaButton) e.getSource();
        SceneID sceneID = areaButton.getSceneId();
        SceneID currentScene = SceneManager.getInstance().getCurrentSceneID();
        Scene nextScene = SceneManager.getInstance().getScene(sceneID);
        if(nextScene == null){
            oldButton.setSelected(true);
            areaButton.setSelected(false);
            System.err.println("COULDN'T CHANGE!");
            return;
        }
        oldButton = areaButton;
        firePropertyChange(SCENE_CHANGE, currentScene, sceneID);
    }
    public void setOldButton(ChangeAreaButton oldButton){
        this.oldButton = oldButton;
    }
}
