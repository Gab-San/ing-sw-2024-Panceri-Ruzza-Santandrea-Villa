package it.polimi.ingsw.view;

/**
 * This class implements the scene identifier generator.
 */
public class SceneID {
    private static final String OPPONENT_SCENE_PREFIX = "OPPONENT_AREA_";
    private final String sceneName;

    private SceneID(String sceneName){
        this.sceneName = sceneName;
    }

    /**
     * Returns true if the scene identifier represents an identifier of an opponent's area scene,
     * false otherwise.
     * @return true if selected id is of an opponent's area scene, false otherwise
     */
    public boolean isOpponentAreaScene(){
        return sceneName.startsWith(OPPONENT_SCENE_PREFIX);
    }

    /**
     * Returns opponent scene's identifier nickname.
     * @return opponent scene's identifier nickname
     */
    public String getOpponentNickname(){
        if(isOpponentAreaScene())
            return sceneName.substring(OPPONENT_SCENE_PREFIX.length());
        else return null;
    }
    @Override
    public boolean equals(Object other){
        if(this == other) return true;
        else if(other instanceof SceneID otherID)
            return this.sceneName.equals(otherID.sceneName);
        else return false;
    }
    @Override
    public int hashCode(){
        return sceneName.hashCode();
    }
    @Override
    public String toString(){
        return isOpponentAreaScene() ? getOpponentNickname() : sceneName;
    }

//region COMMON SCENES

    /**
     * Returns local player area scene identifier.
     * @return local player area scene identifier
     */
    public static SceneID getMyAreaSceneID(){
        return new SceneID("Me");
    }

    /**
     * Returns nickname selection scene's identifier.
     * @return nickname selection scene's identifier
     */
    public static SceneID getNicknameSelectSceneID(){
        return new SceneID("NICKNAME_SELECT");
    }

//endregion

//region Unknown/Not assigned SCENE IDS

    /**
     * Returns notification scene identifier.
     * @return notification scene identifier
     */
    public static SceneID getNotificationSceneID(){return new SceneID("DEFAULT");}

    /**
     * Returns board scene identifier.
     * @return board scene identifier
     */
    public static SceneID getBoardSceneID(){
        return new SceneID("Board");
    }

    /**
     * Returns opponent area scene identifier associated with specified nickname.
     * @param nickname opponent identifier
     * @return opponent area scene identifier
     */
    public static SceneID getOpponentAreaSceneID(String nickname){
        return new SceneID(OPPONENT_SCENE_PREFIX + nickname);
    }

    /**
     * Returns an endgame scene's identifier.
     * @return endgame scene's identifier
     */
    public static SceneID getEndgameSceneID() {
        return new SceneID("ENDGAME_SHOW_WINNERS");
    }

    /**
     * Returns helper scene identifier.
     * @return helper scene identifier
     */
    public static SceneID getHelperSceneID() {
        return new SceneID("COMMAND_HELPER_LEGEND");
    }
//endregion
}
