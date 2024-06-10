package it.polimi.ingsw.view.gui.scenes.extra.playerpanel;

import it.polimi.ingsw.view.Scene;
import it.polimi.ingsw.view.SceneID;

import javax.swing.*;

public class ChangeAreaButton extends JToggleButton {
    private SceneID sceneID;

    public ChangeAreaButton(Action action, SceneID sceneID){
        super(action);
        this.sceneID = sceneID;
    }
    public void setSceneName(String sceneName){
        setText(sceneName);
    }
    public String getSceneName(){
        return getText();
    }
    public void setSceneID(SceneID sceneID){
        this.sceneID = sceneID;
    }

    public SceneID getSceneId(){
        return sceneID;
    }

}
