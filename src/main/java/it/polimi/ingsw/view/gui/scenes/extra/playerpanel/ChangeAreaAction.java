package it.polimi.ingsw.view.gui.scenes.extra.playerpanel;

import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.SceneManager;
import it.polimi.ingsw.view.gui.ChangeNotifications;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ChangeAreaAction extends AbstractAction {
    public static final String SCENE_CHANGE = "SCENE_CHANGE_PROPERTY";
    @Override
    public void actionPerformed(ActionEvent e) {
       assert e.getSource() instanceof ChangeAreaButton;
       ChangeAreaButton areaButton = (ChangeAreaButton) e.getSource();
       SceneID sceneID = areaButton.getSceneId();
       SceneID currentScene = SceneManager.getInstance().getCurrentSceneID();
       firePropertyChange(SCENE_CHANGE, currentScene, sceneID);
    }
}
