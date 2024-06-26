package it.polimi.ingsw.view.gui.scenes.extra.playerpanel;

import it.polimi.ingsw.view.Scene;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.SceneManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This action lets the user change the scene to the selected player's area.
 */
public class ChangeAreaAction extends AbstractAction {
    /**
     * Default constructor.
     */
    public ChangeAreaAction(){
        super();
    }
    private ChangeAreaButton oldButton;
    /**
     * Area scene change property name.
     */
    public static final String SCENE_CHANGE = "SCENE_CHANGE_PROPERTY";

    /**
     * If possible switches to the selected player's area scene.
     * @param e the event to be processed
     */
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

    /**
     * Sets the last pressed button.
     * @param oldButton last pressed button
     */
    public void setOldButton(ChangeAreaButton oldButton){
        this.oldButton = oldButton;
    }
}
