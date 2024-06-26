package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;

/**
 * Collection of ANSI background colors
 */
public class ConsoleBackgroundColors {
    /**
     * The ANSI prefix, common to all codes
     */
    public static final String PREFIX = "\u001B[";
    /**
     * The ANSI postfix, common to all codes
     */
    public static final String POSTFIX = "m";

    /**
     * ANSI code to reset foreground and background to normal <br>
     * (white text on black background)
     */
    public static final String RESET = PREFIX + "00" + POSTFIX;

    //region Normal intensity backgrounds
    /**
     * ANSI code coloring the background
     */
    public static final String WHITE = PREFIX + "47" + POSTFIX;
    /**
     * ANSI code coloring the background
     */
    public static final String RED = PREFIX + "41" + POSTFIX;
    /**
     * ANSI code coloring the background
     */
    public static final String PURPLE = PREFIX + "45" + POSTFIX;
    /**
     * ANSI code coloring the background
     */
    public static final String GREEN = PREFIX + "42" + POSTFIX;
    /**
     * ANSI code coloring the background
     */
    public static final String BLUE = PREFIX + "44" + POSTFIX;
    /**
     * ANSI code coloring the background
     */
    public static final String YELLOW = PREFIX + "43" + POSTFIX;
    //endregion
    //region High Intensity backgrounds
    /**
     * ANSI code coloring the background
     */
    public static final String BLACK_BRIGHT = PREFIX + "0;100" + POSTFIX;
    /**
     * ANSI code coloring the background
     */
    public static final String RED_BRIGHT = PREFIX + "0;101" + POSTFIX;
    /**
     * ANSI code coloring the background
     */
    public static final String GREEN_BRIGHT = PREFIX + "0;102" + POSTFIX;
    /**
     * ANSI code coloring the background
     */
    public static final String YELLOW_BRIGHT = PREFIX + "0;103" + POSTFIX;
    /**
     * ANSI code coloring the background
     */
    public static final String BLUE_BRIGHT = PREFIX + "0;104" + POSTFIX;
    /**
     * ANSI code coloring the background
     */
    public static final String PURPLE_BRIGHT = PREFIX + "0;105" + POSTFIX;
    /**
     * ANSI code coloring the background
     */
    public static final String CYAN_BRIGHT = PREFIX + "0;106" + POSTFIX;
    /**
     * ANSI code coloring the background
     */
    public static final String WHITE_BRIGHT = PREFIX + "0;107" + POSTFIX;
    //endregion
    /**
     * Private constructor, as this class should not be instantiated
     */
    private ConsoleBackgroundColors(){}

    /**
     * Returns the PlayerColor's ANSI background color equivalent string.
     * @param color the PlayerColor to transform into its ANSI equivalent
     * @return PlayerColor's ANSI background color equivalent string
     */
    public static String getColorFromEnum(PlayerColor color){
        if(color == null) return RESET;
        return switch (color){
            case BLUE -> BLUE;
            case RED -> RED;
            case GREEN -> GREEN;
            case YELLOW -> YELLOW;
        };
    }
    /**
     * Returns GameResource's ANSI background color equivalent string.
     * @param resource the GameResource to transform into its ANSI equivalent
     * @return GameResource's ANSI background color equivalent string
     */
    public static String getColorFromEnum(GameResource resource){
        if(resource == null) return YELLOW;
        return switch (resource){
            case MUSHROOM -> RED;
            case BUTTERFLY -> PURPLE;
            case LEAF -> GREEN;
            case WOLF -> BLUE;
            default -> YELLOW;
        };
    }
}
