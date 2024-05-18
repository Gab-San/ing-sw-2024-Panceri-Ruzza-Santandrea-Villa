package it.polimi.ingsw;

public enum PlayerColor {
    BLUE,
    RED,
    YELLOW,
    GREEN;
    public static PlayerColor parseColour(char colour) {
        return switch (colour) {
            case 'B' -> PlayerColor.BLUE;
            case 'R' -> PlayerColor.RED;
            case 'Y' -> PlayerColor.YELLOW;
            case 'G' -> PlayerColor.GREEN;
            default -> null;
        };
    }

    @Override
    public String toString() {
        String normalRep = super.toString();
        return normalRep.charAt(0) + normalRep.substring(1).toLowerCase();
        // this returns 'Blue' instead of 'BLUE'
        //FIXME:[Ale] I think not having all caps looks nicer, but we can remove this if it's problematic
    }
}
