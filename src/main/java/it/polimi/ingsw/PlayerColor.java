package it.polimi.ingsw;
import it.polimi.ingsw.view.GameColor;

import java.awt.*;


/**
 * Enumeration of all the possible colors Players can choose
 * at the start of the game.
 */
public enum PlayerColor {
    /**
     * Blue pawn color.
     */
    BLUE,
    /**
     * Red pawn color.
     */
    RED,
    /**
     * Yellow pawn color.
     */
    YELLOW,
    /**
     * Green pawn color.
     */
    GREEN;

    /**
     * Returns the color associated with the given initial.
     * @param colour the initial of the desired color (uppercase)
     * @return the color associated with the given initial, or null if none match.
     */
    public static PlayerColor parseColour(char colour) {
        return switch (colour) {
            case 'B' -> PlayerColor.BLUE;
            case 'R' -> PlayerColor.RED;
            case 'Y' -> PlayerColor.YELLOW;
            case 'G' -> PlayerColor.GREEN;
            default -> null;
        };
    }


    /**
     * Returns the color associated with the player color. Default value is black.
     * @param colorName player color
     * @return the color associated with the given player color.
     */
    public static Color getColor(PlayerColor colorName) {
        if(colorName == null){
            return Color.black;
        }
        return switch (colorName) {
            case RED -> GameColor.PLAYER_RED.getColor();
            case BLUE -> GameColor.PLAYER_BLUE.getColor();
            case GREEN -> GameColor.PLAYER_GREEN.getColor();
            case YELLOW -> GameColor.PLAYER_YELLOW.getColor();
        };
    }

    /**
     * Override of the standard toString method of enums
     * @return the LowerCamelCase string representation of the enum value, for cleaner printing.
     */
    @Override
    public String toString() {
        String normalRep = super.toString();
        return normalRep.charAt(0) + normalRep.substring(1).toLowerCase();
        // this returns 'Blue' instead of 'BLUE'
    }
}
