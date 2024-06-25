package it.polimi.ingsw.view;

/**
 * This class implements the scene identifier generator. <br>
 * The only available IDs are the ones provided by the static getters of this class.
 */
public class SceneID {
    /**
     * A unique prefix to opponent scenes to prevent issues that could arise
     * with opponents having a nickname like "Board"
     */
    private static final String OPPONENT_SCENE_PREFIX = "OPPONENT_AREA_";
    /**
     * The scene ID (a unique string)
     */
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
     * @return
     * <ul>
     *     <li>
     *          null if this ID is not an OpponentAreaScene ID. <br>
     *     </li>
     *     <li>
     *         the opponent's nickname if it is an OpponentAreaScene ID.
     *     </li>
     * </ul>
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

//region COMMON SCENE IDs
    /**
     * @return local player area scene identifier
     */
    public static SceneID getMyAreaSceneID(){
        return new SceneID("Me");
    }
    /**
     * @return nickname selection scene's identifier
     */
    public static SceneID getNicknameSelectSceneID(){
        return new SceneID("NICKNAME_SELECT");
    }
    /**
     * @return board scene identifier
     */
    public static SceneID getBoardSceneID(){
        return new SceneID("Board");
    }
    /**
     * Returns opponent area scene identifier associated with the specified nickname.
     * @param nickname opponent identifier
     * @return opponent area scene identifier
     */
    public static SceneID getOpponentAreaSceneID(String nickname){
        return new SceneID(OPPONENT_SCENE_PREFIX + nickname);
    }
    /**
     * @return endgame scene's identifier
     */
    public static SceneID getEndgameSceneID() {
        return new SceneID("ENDGAME_SHOW_WINNERS");
    }
//endregion

//region TUI-ONLY SCENE IDs

    /**
     * @return the ID of the generic empty "notification scene", used on TUI
     *         to display all notifications independently of the current scene
     */
    public static SceneID getNotificationSceneID(){
        return new SceneID("DEFAULT");
    }

    /**
     * @return the TUI helper scene identifier
     */
    public static SceneID getHelperSceneID() {
        return new SceneID("COMMAND_HELPER_LEGEND");
    }
//endregion
}
