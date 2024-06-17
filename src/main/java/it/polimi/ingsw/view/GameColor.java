package it.polimi.ingsw.view;

import java.awt.*;

/**
 * This enumeration class stores all the color values used into the gui
 */
public enum GameColor {
    /**
     * Color used by the board background.
     */
    BOARD_COLOUR(new Color(0xCC9866)),
    /**
     * Color used by error notifications.
     */
    ERROR_COLOUR(new Color(0x960B0B)),
    /**
     * Color used by to display notifications.
     */
    NOTIFICATION_COLOUR(new Color(0x0C8D27)),
    /**
     * Red color for player
     */
    PLAYER_RED(new Color(0xf23020)),
    /**
     * Blue color for player
     */
    PLAYER_BLUE(new Color(0x072bce)),
    /**
     * Green color for player
     */
    PLAYER_GREEN(new Color(0x07c912)),
    PLAYER_YELLOW(new Color(0xf4fb14));

    private final Color color;

    GameColor(Color color){
        this.color = color;
    }

    /**
     * Returns the color associated with the enum value.
     * @return associated color
     */
    public Color getColor() {
        return color;
    }
}
