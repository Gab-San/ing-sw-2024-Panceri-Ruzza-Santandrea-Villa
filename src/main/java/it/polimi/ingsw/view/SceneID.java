package it.polimi.ingsw.view;

public class SceneID {
    final String sceneName;

    private SceneID(String sceneName){
        this.sceneName = sceneName;
    }

    @Override
    public boolean equals(Object other){
        if(this == other) return true;
        else if(other instanceof SceneID otherID)
            return this.sceneName.equals(otherID.sceneName);
        else return false;
    }

    public static SceneID getBoardSceneID(){
        return new SceneID("BOARD");
    }
    public static SceneID getMyAreaSceneID(){
        return new SceneID("MY_AREA");
    }
    public static SceneID getOpponentAreaSceneID(String nickname){
        return new SceneID("OPPONENT_AREA_" + nickname);
    }
}
