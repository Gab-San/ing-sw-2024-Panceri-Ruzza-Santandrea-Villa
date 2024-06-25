package it.polimi.ingsw.view;

import it.polimi.ingsw.view.gui.scenes.areas.AreaScene;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class implements a scene manager. A scene manager has full control
 * on game scenes, and it is the only way to access them.
 * It is implemented as a singleton. <br>
 * A scene should first be saved, then set to be displayed. <br>
 */
public class SceneManager {
    /**
     * The scene that is currently displayed
     */
    private Scene currentScene;
    /**
     * Lock to synchronize access to the loaded scenes
     */
    private final Object SCENE_LOCK = new Object();

    /**
     * Map associating an ID with its corresponding (loaded) scene.
     */
    private final Map<SceneID, Scene> savedScenes;
    /**
     * Singleton instance of the SceneManager
     */
    private static SceneManager instance;

    /**
     * Creates a SceneManager with no loaded scenes
     */
    private SceneManager(){
        currentScene = null;
        savedScenes = new HashMap<>();
    }

    /**
     * @return the SceneManager singleton instance
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
     * @return scene associated to scene id (can be null)
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
     * @return currently displayed scene
     */
    public Scene getCurrentScene(){
        synchronized (SCENE_LOCK) {
            return currentScene;
        }
    }

    /**
     * @return the current scene identifier
     * @throws IllegalStateException if the current scene was not loaded
     */
    public SceneID getCurrentSceneID() throws IllegalStateException{
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
     * Removes the scene associated to the scene identifier from the saved scenes.
     * @param sceneID scene identifier
     */
    public void remove(SceneID sceneID){
        synchronized (savedScenes){
            savedScenes.remove(sceneID);
        }
    }

    /**
     * Removes the selected scene from the saved scenes.
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
     * @return a list of all the {@link AreaScene} that were saved before the call to this method. <br>
     *      The returned list is not a view of the loaded scenes and any changes to the
     *      returned list will not be reflected in the SceneManager.
     */
    public List<AreaScene> getAreaScenes() {
        List<AreaScene> scenes = new LinkedList<>();
        synchronized (savedScenes){
            for(SceneID id : savedScenes.keySet()){
                if(id.isOpponentAreaScene() || id.equals(SceneID.getMyAreaSceneID())){
                    scenes.add((AreaScene) savedScenes.get(id));
                }
            }
        }
        return scenes;
    }
}
