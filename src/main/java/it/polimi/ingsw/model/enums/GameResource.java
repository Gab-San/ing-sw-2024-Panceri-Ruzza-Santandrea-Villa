package it.polimi.ingsw.model.enums;

/**
 * Enumeration of game's resources. Valid resources have an associated integer value for array indexing.
 */
public enum GameResource {
    MUSHROOM(1), BUTTERFLY(2),LEAF(3),
    WOLF(4), SCROLL(5),POTION(6),QUILL(7),
    FILLED(-1);
    private final int resourceIndex;
    private GameResource(int resourceIndex){
        this.resourceIndex = resourceIndex;
    }
    public static GameResource getResourceFromName(String resName){
        switch (resName){
            case "MUSHROOM": return GameResource.MUSHROOM;
            case "BUTTERFLY": return GameResource.BUTTERFLY;
            case "LEAF" : return GameResource.LEAF;
            case "WOLF": return GameResource.WOLF;
            case "SCROLL": return GameResource.SCROLL;
            case "POTION" : return GameResource.POTION;
            case "QUILL": return GameResource.QUILL;
            case "FILLED": return GameResource.FILLED;
            default: return null;
        }
    }
    public int getResourceIndex(){
        return resourceIndex - 1;
    }
}
