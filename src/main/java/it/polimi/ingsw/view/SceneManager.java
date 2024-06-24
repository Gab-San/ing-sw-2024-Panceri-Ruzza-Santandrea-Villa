package it.polimi.ingsw.view;

import it.polimi.ingsw.view.gui.scenes.areas.AreaScene;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//DOCS add docs
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
        Scene newScene = getScene(sceneID);
        if(newScene == null){
            throw new IllegalArgumentException("Scene " + sceneID + " does not exist.");
        }
        setScene(newScene);
    }

    public Scene getCurrentScene(){
        synchronized (SCENE_LOCK) {
            return currentScene;
        }
    }

    public SceneID getCurrentSceneID(){
        synchronized (SCENE_LOCK){
            synchronized (loadedScenes){
                for(SceneID id : loadedScenes.keySet()){
                    if(currentScene.equals(loadedScenes.get(id))){
                        return id;
                    }
                }
            }
        }
        throw new IllegalStateException("No current scene selected");
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

    public List<AreaScene> getAreaScenes() {
        List<AreaScene> scenes = new LinkedList<>();
        synchronized (loadedScenes){
            for(SceneID id : loadedScenes.keySet()){
                if(id.isOpponentAreaScene()){
                    scenes.add((AreaScene) loadedScenes.get(id));
                }
                if(id.equals(SceneID.getMyAreaSceneID())){
                    scenes.add((AreaScene) loadedScenes.get(id));
                }
            }
        }
        return scenes;
    }
}
