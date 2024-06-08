package it.polimi.ingsw.view;

import it.polimi.ingsw.GamePhase;

import java.util.Map;

public class SceneID {
    private static final String OPPONENT_SCENE_PREFIX = "OPPONENT_AREA_";
    private final String sceneName;

    private SceneID(String sceneName){
        this.sceneName = sceneName;
    }


    public boolean isOpponentAreaScene(){
        return sceneName.startsWith(OPPONENT_SCENE_PREFIX);
    }
    public String getNickname(){
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
        return isOpponentAreaScene() ? getNickname() : sceneName;
    }

//region COMMON SCENES
    public static SceneID getMyAreaSceneID(){
        return new SceneID("Me");
    }
    public static SceneID getNicknameSelectSceneID(){
        return new SceneID("NICKNAME_SELECT");
    }

//endregion

//region GUI-ONLY SCENE IDS
    public static SceneID getSetNumberOfPlayersID() {
    return new SceneID("SET_NUMBER_OF_PLAYERS");
}

//endregion

//region TUI-ONLY SCENE IDS

//endregion

//region Unknown/Not assigned SCENE IDS
    public static SceneID getNotificationSceneID(){return new SceneID("DEFAULT");}
    public static SceneID getBoardSceneID(){
        return new SceneID("Board");
    }
    public static SceneID getOpponentAreaSceneID(String nickname){
        return new SceneID(OPPONENT_SCENE_PREFIX + nickname);
    }
    public static SceneID getEndgameSceneID() {
        return new SceneID("ENDGAME_SHOW_WINNERS");
    }
    public static SceneID getHelperSceneID() {
        return new SceneID("COMMAND_HELPER_LEGEND");
    }
//endregion
}
