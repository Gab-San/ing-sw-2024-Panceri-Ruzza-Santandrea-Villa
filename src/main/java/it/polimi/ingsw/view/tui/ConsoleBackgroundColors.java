package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;

public class ConsoleBackgroundColors {
  public static final String RESET = "\u001B[00m";  //Resetta il colore

    // Normal intensity backgrounds
  public static final String WHITE = "\u001B[47m"; //Colore bianco per i bordi
  public static final String RED = "\u001B[41m";
  public static final String PURPLE = "\u001B[45m";
  public static final String GREEN = "\u001B[42m";
  public static final String BLUE = "\033[44m";
  public static final String YELLOW = "\033[43m";

    // High Intensity backgrounds
  public static final String BLACK_BRIGHT = "\033[0;100m";
  public static final String RED_BRIGHT = "\033[0;101m";
  public static final String GREEN_BRIGHT = "\033[0;102m";
  public static final String YELLOW_BRIGHT = "\033[0;103m";
  public static final String BLUE_BRIGHT = "\033[0;104m";
  public static final String PURPLE_BRIGHT = "\033[0;105m";
  public static final String CYAN_BRIGHT = "\033[0;106m";
  public static final String WHITE_BRIGHT = "\033[0;107m";

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
