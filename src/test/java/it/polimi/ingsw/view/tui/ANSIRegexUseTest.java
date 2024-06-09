package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.RESET;
import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.WHITE;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.BLACK_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.getColorFromEnum;
import static it.polimi.ingsw.view.tui.ConsoleColorsCombiner.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ANSIRegexUseTest {
    @Test
    void testANSICodeRemovalViaRegex(){
        System.out.println("RegexAnyColor is: " + getRegexAnyColor());

        String testString = getColorFromEnum(PlayerColor.RED) + "RED TEXT" + RESET;
        assertTrue(testString.matches(".*(" + getRegexAnyColor() + ").*"), "string should contain an ANSI code");
        System.out.println("Not replaced PlayerColor: " + testString);
        testString = removeAllANSIColors(testString);
        System.out.println("Replaced PlayerColor: " + testString);
        assertFalse(testString.matches(".*(" + getRegexAnyColor() + ").*"), "all ANSI codes should've been removed");

        testString = getColorFromEnum(GameResource.BUTTERFLY) + " PURPLE TEXT " + RESET;
        assertTrue(testString.matches(".*(" + getRegexAnyColor() + ").*"), "string should contain an ANSI code");
        System.out.println("Not replaced GameResource: " + testString);
        testString = removeAllANSIColors(testString);
        System.out.println("Replaced GameResource: " + testString);
        assertFalse(testString.matches(".*(" + getRegexAnyColor() + ").*"), "all ANSI codes should've been removed");

        testString = combine(BLACK_TEXT, WHITE) + " BLACK TEXT ON WHITE BACKGROUND " + RESET;
        assertTrue(testString.matches(".*(" + getRegexAnyColor() + ").*"), "string should contain an ANSI code");
        System.out.println("Not replaced Corner color: " + testString);
        testString = removeAllANSIColors(testString);
        System.out.println("Replaced Corner color: " + testString);
        assertFalse(testString.matches(".*(" + getRegexAnyColor() + ").*"), "all ANSI codes should've been removed");
    }
}
