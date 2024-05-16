package it.polimi.ingsw;

/**
 * Enumeration of game's resources. Valid resources have an associated integer value for array indexing.
 */
public enum GameResource {
    MUSHROOM(1), BUTTERFLY(2), LEAF(3),
    WOLF(4), SCROLL(5), POTION(6),
    QUILL(7), FILLED(-1);
    private final int resourceIndex;
    private GameResource(int resourceIndex){
        this.resourceIndex = resourceIndex;
    }
    public static GameResource getResourceFromName(String resName){
        return switch (resName) {
            case "MUSHROOM" -> GameResource.MUSHROOM;
            case "BUTTERFLY" -> GameResource.BUTTERFLY;
            case "LEAF" -> GameResource.LEAF;
            case "WOLF" -> GameResource.WOLF;
            case "SCROLL" -> GameResource.SCROLL;
            case "POTION" -> GameResource.POTION;
            case "QUILL" -> GameResource.QUILL;
            case "FILLED" -> GameResource.FILLED;
            default -> null;
        };
    }
    public static GameResource getResourceFromNameInitial(String resName){
        return switch (resName) {
            case "M" -> GameResource.MUSHROOM;
            case "B" -> GameResource.BUTTERFLY;
            case "L" -> GameResource.LEAF;
            case "W" -> GameResource.WOLF;
            case "S" -> GameResource.SCROLL;
            case "P" -> GameResource.POTION;
            case "Q" -> GameResource.QUILL;
            case "F" -> GameResource.FILLED;
            default -> null;
        };
    }
    public static GameResource getResourceFromColor(String colorName){
        return switch (colorName.toUpperCase()){
            case "RED" -> MUSHROOM;
            case "PURPLE" -> BUTTERFLY;
            case "GREEN" -> LEAF;
            case "BLUE" -> WOLF;
            default -> null;
        };
    }
    public String toString(){
        return switch (this) {
            case MUSHROOM -> "M";
            case BUTTERFLY -> "B";
            case LEAF -> "L";
            case WOLF -> "W";
            case SCROLL -> "S";
            case POTION -> "P";
            case QUILL -> "Q";
            case FILLED -> "F";
        };
    }
    public String asColor(){
        return switch (this) {
            case MUSHROOM -> "R";
            case BUTTERFLY -> "P";
            case LEAF -> "G";
            case WOLF -> "B";
            default -> "*";
        };
    }

    public int getResourceIndex(){
        return resourceIndex - 1;
    }
}

