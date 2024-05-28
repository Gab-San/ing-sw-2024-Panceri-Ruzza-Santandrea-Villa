package it.polimi.ingsw.view.tui;

import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.POSTFIX;
import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.PREFIX;

public class ConsoleColorsCombiner {
    public static String combine(String foreground, String background){
        return  PREFIX +
                foreground.replace(PREFIX, "").replace(POSTFIX, "") +
                background.replace(PREFIX, ";").replace(POSTFIX,"")
                + POSTFIX
                ;
    }
}
