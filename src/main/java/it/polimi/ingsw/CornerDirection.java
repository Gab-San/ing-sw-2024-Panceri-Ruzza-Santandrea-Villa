package it.polimi.ingsw;

import java.util.Comparator;

/**
 * Enumeration of possible direction where a corner can be (all diagonals from the center of the card)
 */
public enum CornerDirection {
    /**
     * Top left corner
     */
    TL(0),
    /**
     * Top right corner
     */
    TR(1),
    /**
     * Bottom left corner
     */
    BL(2),
    /**
     * Bottom right corner
     */
    BR(3);

    private final int index;
    CornerDirection(int index){
        this.index = index;
    }

    /**
     * Returns the index associated to this corner.
     * @return integer associated to this corner
     */
    public int getIndex(){ return index; }

    /**
     * Returns the direction opposite to the corner on whose this method is called.
     * @return the direction opposite to this <br> TR -> BL ; BL -> TR ;<br> BR -> TL ; TL -> BR
     */
    public CornerDirection opposite(){
        return switch (this) {
            case TL -> BR;
            case TR -> BL;
            case BL -> TR;
            case BR -> TL;
        };
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

    /**
     * Returns the comparator for corners.
     *
     * <p>
     *     The corner comparator uses indexes to order the corner.
     * </p>
     * @return corners direction comparator
     */
    public static Comparator<CornerDirection> getComparator(){
        return Comparator.comparingInt(CornerDirection::getIndex);
    }

}
