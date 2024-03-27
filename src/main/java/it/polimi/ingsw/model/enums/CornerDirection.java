package it.polimi.ingsw.model.enums;

public enum CornerDirection {
    TL("TOP LEFT"), TR("TOP RIGHT"), BL("BOTTOM LEFT"), BR("BOTTOM RIGHT");
    // Could be useful for debugging information or to display to the player
    private final String extendedName;
    private CornerDirection(String extendedName){
        this.extendedName = extendedName;
    }

    @Override
    public String toString() {
        return extendedName;
    }
}
