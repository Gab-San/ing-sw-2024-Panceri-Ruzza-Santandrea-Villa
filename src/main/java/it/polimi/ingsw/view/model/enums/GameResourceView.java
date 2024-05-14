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
    public static it.polimi.ingsw.view.model.enums.GameResourceView getResourceFromName(String resName){
        switch (resName){
            case "M": return it.polimi.ingsw.view.model.enums.GameResourceView.M;
            case "B": return it.polimi.ingsw.view.model.enums.GameResourceView.B;
            case "L" : return it.polimi.ingsw.view.model.enums.GameResourceView.L;
            case "W": return it.polimi.ingsw.view.model.enums.GameResourceView.W;
            case "S": return it.polimi.ingsw.view.model.enums.GameResourceView.S;
            case "P" : return it.polimi.ingsw.view.model.enums.GameResourceView.P;
            case "Q": return it.polimi.ingsw.view.model.enums.GameResourceView.Q;
            case "F": return it.polimi.ingsw.view.model.enums.GameResourceView.F;
            default: return null;
        }
    }

    public int getResourceIndex(){
        return resourceIndex - 1;
    }
}

