package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;

public class ConsoleBackgroundColors {
    public static final String PREFIX = "\u001B[";
    public static final String POSTFIX = "m";
    
    public static final String RESET = PREFIX + "00" + POSTFIX;  //Resetta il colore

    // Normal intensity backgrounds
    public static final String WHITE = PREFIX + "47" + POSTFIX; //Colore bianco per i bordi
    public static final String RED = PREFIX + "41" + POSTFIX;
    public static final String PURPLE = PREFIX + "45" + POSTFIX;
    public static final String GREEN = PREFIX + "42" + POSTFIX;
    public static final String BLUE = PREFIX + "44" + POSTFIX;
    public static final String YELLOW = PREFIX + "43" + POSTFIX;

    // High Intensity backgrounds
    public static final String BLACK_BRIGHT = PREFIX + "0;100" + POSTFIX;
    public static final String RED_BRIGHT = PREFIX + "0;101" + POSTFIX;
    public static final String GREEN_BRIGHT = PREFIX + "0;102" + POSTFIX;
    public static final String YELLOW_BRIGHT = PREFIX + "0;103" + POSTFIX;
    public static final String BLUE_BRIGHT = PREFIX + "0;104" + POSTFIX;
    public static final String PURPLE_BRIGHT = PREFIX + "0;105" + POSTFIX;
    public static final String CYAN_BRIGHT = PREFIX + "0;106" + POSTFIX;
    public static final String WHITE_BRIGHT = PREFIX + "0;107" + POSTFIX;

    public static String getColorFromEnum(PlayerColor color){
        if(color == null) return RESET;
        return switch (color){
            case BLUE -> BLUE;
            case RED -> RED;
            case GREEN -> GREEN;
            case YELLOW -> YELLOW;
        };
    }
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
