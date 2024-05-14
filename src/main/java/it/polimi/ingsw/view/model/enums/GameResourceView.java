package it.polimi.ingsw.view.model.enums;
/**
 * Enumeration of game's resources. Valid resources have an associated integer value for array indexing.
 */
public enum GameResourceView {
    M(1), B(2), L(3), 
    W(4), S(5), P(6), 
    Q(7), F(-1);
    private final int resourceIndex;
    private GameResourceView(int resourceIndex){
        this.resourceIndex = resourceIndex;
    }
    public static GameResourceView getResourceFromName(String resName){
        return switch (resName) {
            case "M" -> GameResourceView.M;
            case "B" -> GameResourceView.B;
            case "L" -> GameResourceView.L;
            case "W" -> GameResourceView.W;
            case "S" -> GameResourceView.S;
            case "P" -> GameResourceView.P;
            case "Q" -> GameResourceView.Q;
            case "F" -> GameResourceView.F;
            default -> null;
        };
    }
    public String toString(){
        return switch (this) {
            case M -> "M";
            case B -> "B";
            case L -> "L";
            case W -> "W";
            case S -> "S";
            case P -> "P";
            case Q -> "Q";
            case F -> "F";
        };
    }

    public int getResourceIndex(){
        return resourceIndex - 1;
    }
}

