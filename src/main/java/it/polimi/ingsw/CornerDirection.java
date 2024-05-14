package it.polimi.ingsw;

/**
 * Enumeration of possible direction where a corner can be (all diagonals from the center of the card)
 */
public enum CornerDirection {
    TL("TL"), TR("TR"), BL("BL"), BR("BR");
    // Could be useful for debugging information or to display to the player
    private final String stringId;
    CornerDirection(String stringId){
        this.stringId = stringId;
    }

    @Override
    public String toString() {
        return stringId;
    }

    /**
     * @return the direction opposite to this <br> (TR -> BL ; BL -> TR ;<br> BR -> TL ; TL -> BR)
     */
    public CornerDirection opposite(){
        switch (this){
            default: // never triggered, but necessary to compile
            case TL: return BR;
            case TR: return BL;
            case BL: return TR;
            case BR: return TL;
        }
    }

    /**
     * Translates a direction described by a string to a direction as the enum value
     * @param dirName the direction as a string
     * @return the direction as an enum value
     * @throws IllegalArgumentException if the string does not describe a valid direction
     */
    public static CornerDirection getDirectionFromString(String dirName) throws IllegalArgumentException{
        return switch (dirName) {
            case "TL" -> TL;
            case "TR" -> TR;
            case "BR" -> BR;
            case "BL" -> BL;
            default -> throw new IllegalArgumentException("Invalid direction name");
        };
    }
}
