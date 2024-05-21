package it.polimi.ingsw.view;

import java.util.Hashtable;
import java.util.Map;


public abstract class View {
    private Scene currentScene;
    private final Map<SceneID, Scene> sceneIDMap;

    protected View(Scene currentScene) {
        this.currentScene = currentScene;
        sceneIDMap = new Hashtable<>();
    }

    public abstract void update(SceneID sceneID);

}
