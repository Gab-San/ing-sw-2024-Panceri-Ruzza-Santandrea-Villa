package it.polimi.ingsw.view.gui.scenes.extra.playerpanel;

import it.polimi.ingsw.view.SceneID;

import javax.swing.*;

/**
 * This class represents a toggle button that when pressed displays the associated player area.
 */
public class ChangeAreaButton extends JToggleButton {
    private final SceneID sceneID;

    /**
     * Constructs a change area button.
     * @param action button action
     * @param sceneID identifier of the player area
     */
    public ChangeAreaButton(Action action, SceneID sceneID){
        super(action);
        this.sceneID = sceneID;
    }

    /**
     * Returns the name associated with the player's area (usually the player's id).
     * @return player's area nickname
     */
    public String getSceneName(){
        return getText();
    }

    /**
     * Returns the scene id associated with the embedded player area scene.
     * @return player's area scene id
     */
    public SceneID getSceneId(){
        return sceneID;
    }

}
