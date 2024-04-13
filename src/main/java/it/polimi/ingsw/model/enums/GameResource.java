package it.polimi.ingsw.model.enums;

public enum GameResource {
    MUSHROOM(1), BUTTERFLY(2),LEAF(3),
    WOLF(4), SCROLL(5),POTION(6),QUILL(7),
    FILLED(-1);
    private final int resourceIndex;
    private GameResource(int resourceIndex){
        this.resourceIndex = resourceIndex;
    }
    public int getResourceIndex(){
        return resourceIndex - 1;
    }
}
