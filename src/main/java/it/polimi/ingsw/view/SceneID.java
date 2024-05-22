package it.polimi.ingsw.view;

public class SceneID {
    private static final String OPPONENT_SCENE_PREFIX = "OPPONENT_AREA_";
    final String sceneName;

    private SceneID(String sceneName){
        this.sceneName = sceneName;
    }

    public boolean isOpponentAreaScene(){
        return sceneName.contains(OPPONENT_SCENE_PREFIX);
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

//    public static SceneID getConnectTechSceneID(){
//        return new SceneID("CONNECT_TECH");
//    }
    public static SceneID getNicknameSelectSceneID(){
        return new SceneID("NICKNAME_SELECT");
    }

    public static SceneID getBoardSceneID(){
        return new SceneID("BOARD");
    }
    public static SceneID getMyAreaSceneID(){
        return new SceneID("MY_AREA");
    }
    public static SceneID getOpponentAreaSceneID(String nickname){
        return new SceneID(OPPONENT_SCENE_PREFIX + nickname);
    }
}
