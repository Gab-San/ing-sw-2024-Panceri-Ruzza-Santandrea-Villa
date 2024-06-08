package it.polimi.ingsw.view;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private Scene currentScene;
    private final Object SCENE_LOCK = new Object();
    private final Map<SceneID, Scene> loadedScenes;
    private static SceneManager instance;
    private SceneManager(){
        currentScene = null;
        loadedScenes = new HashMap<>();
    }

    public synchronized static SceneManager getInstance(){
        if(instance == null) instance = new SceneManager();
        return instance;
    }

    public void loadScene(SceneID sceneID, Scene scene){
        synchronized (loadedScenes) {
            loadedScenes.put(sceneID, scene);
        }
    }

    public Scene getScene(SceneID sceneID){
        synchronized (loadedScenes) {
            return loadedScenes.get(sceneID);
        }
    }

    public void setScene(Scene scene){
        synchronized (SCENE_LOCK) {
            currentScene = scene;
            currentScene.display();
        }
    }

    public void setScene(SceneID sceneID) throws IllegalArgumentException{
        Scene newScene;
        synchronized (loadedScenes) {
            newScene = loadedScenes.get(sceneID);
        }
        if(newScene == null){
            throw new IllegalArgumentException("Scene " + sceneID + " does not exist.");
        }
        synchronized (SCENE_LOCK) {
            currentScene = newScene;
            currentScene.display();
        }
    }

    public Scene getCurrentScene(){
        synchronized (SCENE_LOCK) {
            return currentScene;
        }
    }

    public void remove(SceneID sceneID){
        synchronized (loadedScenes){
            loadedScenes.remove(sceneID);
        }
    }
    public void remove(Scene scene){
        synchronized (loadedScenes){
            for(SceneID sceneID: loadedScenes.keySet()) {
                if(scene == loadedScenes.get(sceneID)) {
                    loadedScenes.remove(sceneID);
                }
            }
        }
    }

}
