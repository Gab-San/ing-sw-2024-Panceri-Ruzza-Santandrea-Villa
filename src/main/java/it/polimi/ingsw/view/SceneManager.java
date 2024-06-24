package it.polimi.ingsw.view;

import it.polimi.ingsw.view.gui.scenes.areas.AreaScene;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class implements a scene manager. A scene manager has full control
 * on game scenes, and it is the only way to access them. It is implemented
 * as a singleton.
 */
public class SceneManager {
    private Scene currentScene;
    private final Object SCENE_LOCK = new Object();
    private final Map<SceneID, Scene> savedScenes;
    private static SceneManager instance;
    private SceneManager(){
        currentScene = null;
        savedScenes = new HashMap<>();
    }

    /**
     * Returns scene manager singleton instance.
     * @return singleton instance
     */
    public synchronized static SceneManager getInstance(){
        if(instance == null) instance = new SceneManager();
        return instance;
    }

    /**
     * Saves the specified scene.
     * @param sceneID associated scene identifier
     * @param scene scene to save
     */
    public void saveScene(SceneID sceneID, Scene scene){
        synchronized (savedScenes) {
            savedScenes.put(sceneID, scene);
        }
    }

    /**
     * Returns the scene associated to the scene identifier.
     * @param sceneID scene identifier
     * @return scene associated to scene id
     */
    public Scene getScene(SceneID sceneID){
        synchronized (savedScenes) {
            return savedScenes.get(sceneID);
        }
    }

    /**
     * Loads the scene and displays it.
     * @param scene scene to load
     */
    public void loadScene(@NotNull Scene scene){
        synchronized (SCENE_LOCK) {
            currentScene = scene;
            currentScene.display();
        }
    }

    /**
     * Loads the scene associated with the scene id.
     * @param sceneID scene to load identifier
     * @throws IllegalArgumentException if the selected scene wasn't saved
     */
    public void loadScene(SceneID sceneID) throws IllegalArgumentException{
        Scene newScene = getScene(sceneID);
        if(newScene == null){
            throw new IllegalArgumentException("Scene " + sceneID + " does not exist.");
        }
        loadScene(newScene);
    }

    /**
     * Returns the current displaying scene.
     * @return current displaying scene
     */
    public Scene getCurrentScene(){
        synchronized (SCENE_LOCK) {
            return currentScene;
        }
    }

    /**
     * Returns the current scene identifier.
     * @return current scene identifier
     */
    public SceneID getCurrentSceneID(){
        synchronized (SCENE_LOCK){
            synchronized (savedScenes){
                for(SceneID id : savedScenes.keySet()){
                    if(currentScene.equals(savedScenes.get(id))){
                        return id;
                    }
                }
            }
        }
        throw new IllegalStateException("No current scene selected");
    }

    /**
     * Removes the scene associated to the scene identifier.
     * @param sceneID scene identifier
     */
    public void remove(SceneID sceneID){
        synchronized (savedScenes){
            savedScenes.remove(sceneID);
        }
    }

    /**
     * Removes the selected scene.
     * @param scene scene to remove
     */
    public void remove(Scene scene){
        synchronized (savedScenes){
            for(SceneID sceneID: savedScenes.keySet()) {
                if(scene == savedScenes.get(sceneID)) {
                    savedScenes.remove(sceneID);
                }
            }
        }
    }

    /**
     * Returns the saved play area scenes.
     * @return saved play area scenes
     */
    public List<AreaScene> getAreaScenes() {
        List<AreaScene> scenes = new LinkedList<>();
        synchronized (savedScenes){
            for(SceneID id : savedScenes.keySet()){
                if(id.isOpponentAreaScene()){
                    scenes.add((AreaScene) savedScenes.get(id));
                }
                if(id.equals(SceneID.getMyAreaSceneID())){
                    scenes.add((AreaScene) savedScenes.get(id));
                }
            }
        }
        return scenes;
    }
}
