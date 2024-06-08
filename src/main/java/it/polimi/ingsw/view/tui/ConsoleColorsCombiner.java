package it.polimi.ingsw.view.tui;

import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.POSTFIX;
import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.PREFIX;

public class ConsoleColorsCombiner {
    /**
     * Combines a TextColor and a Background color
     * to apply both effects simultaneously
     * @param foreground the TextColor
     * @param background the BackgroundColor
     * @return the ANSI combination of both effects
     */
    public static String combine(String foreground, String background){
        return  PREFIX +
                foreground.replace(PREFIX, "").replace(POSTFIX, "") +
                background.replace(PREFIX, ";").replace(POSTFIX,"")
                + POSTFIX
                ;
    }
}
