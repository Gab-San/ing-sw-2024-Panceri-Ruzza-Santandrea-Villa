package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.*;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.BLACK_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.PURPLE_TEXT;

/**
 * Helper class with methods to manipulate ANSI color codes.
 */
public class ConsoleColorsCombiner {
    /**
     * A regex that matches any ANSI background color code associated with a PlayerColor or GameResource. <br>
     * Also matches RESET, WHITE, BLACK_TEXT, PURPLE_TEXT codes. <br>
     * This attribute saves the regex, so it only needs to be computed once on its first use.
     */
    private static String regexAnyColor = null;

    /**
     * Private constructor, as this class should not be instantiated
     */
    private ConsoleColorsCombiner(){}

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

    /**
     * Replaces all '[' in the given ansi code to '\[' <br>
     * The escaping of '[' is necessary to prevent errors in a regex.
     * @param ansi the ANSI code to format
     * @return the formatted code with all brackets escaped as '\[', only to be used in a regex
     */
    public static String formatANSIforRegexUse(String ansi){
        return ansi.replaceAll("\\[", "\\\\[");
        // the 4 \\\\ are needed to insert a proper "\" in the replacement string
        // only using 2 \\ would just escape the [ and not work
    }

    /**
     * Removes all ANSI codes related to any PlayerColor or GameResource. <br>
     * Also removes all RESET, WHITE, BLACK_TEXT, PURPLE_TEXT codes.
     * @param string string from which to remove ANSI codes
     * @return the string without any ANSI codes as specified above
     */
    public static String removeAllANSIColors(String string){
        return string.replaceAll(getRegexAnyColor(), "");
    }

    /**
     * Only builds regexAnyColor once at the first call of this function.
     * @return a regex that matches any ANSI background color code associated with a PlayerColor or GameResource.
     * Also matches RESET, WHITE, BLACK_TEXT, PURPLE_TEXT codes.
     */
    public static String getRegexAnyColor(){
        if(regexAnyColor == null) {
            Set<String> allColors = new HashSet<>();
            allColors.addAll(Arrays.stream(PlayerColor.values())
                    .map(ConsoleBackgroundColors::getColorFromEnum).toList());
            allColors.addAll(Arrays.stream(GameResource.values())
                    .map(ConsoleBackgroundColors::getColorFromEnum).toList());
            allColors.add(RESET);
            allColors.add(WHITE);
            allColors.add(combine(BLACK_TEXT, WHITE));
            allColors.add(combine(PURPLE_TEXT, WHITE));
            allColors.add(PURPLE_TEXT);

            // any duplicates are automatically handled by the Set
            regexAnyColor = allColors.stream()
                    .map(ConsoleColorsCombiner::formatANSIforRegexUse)
                    .collect(Collectors.joining("|"));
        }
        return regexAnyColor;
    }
}
