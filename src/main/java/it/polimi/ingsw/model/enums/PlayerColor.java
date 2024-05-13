package it.polimi.ingsw.model.enums;

public enum PlayerColor {
    BLUE('B'),
    RED('R'),
    YELLOW('Y'),
    GREEN('G');
    private final char colourInitial;
    PlayerColor(char colourInitial){
        this.colourInitial = colourInitial;
    }
    public static PlayerColor parseColour(char colour) {
        return switch (colour) {
            case 'B' -> PlayerColor.BLUE;
            case 'R' -> PlayerColor.RED;
            case 'Y' -> PlayerColor.YELLOW;
            case 'G' -> PlayerColor.GREEN;
            default -> null;
        };
    }
}
