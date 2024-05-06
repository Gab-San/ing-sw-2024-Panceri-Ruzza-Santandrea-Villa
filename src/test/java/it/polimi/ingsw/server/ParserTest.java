package it.polimi.ingsw.server;

import org.junit.jupiter.api.Test;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void patternTest(){
        assertAll(
                () -> assertTrue(Pattern.matches("[2-4]", "2")),
                () -> assertFalse(Pattern.matches("[2-4]", "0")),
                () -> assertFalse(Pattern.matches("2-4", "2")),
                () -> assertTrue(Pattern.matches("[RGrg][0-2]", "R2")),
                () -> assertTrue(Pattern.matches("[RGrg][0-2]", "G2")),
                () -> assertTrue(Pattern.matches("[RGrg][0-2]", "R0")),
                () -> assertTrue(Pattern.matches("[RGrg][0-2]", "r1")),
                () -> assertTrue(Pattern.matches("[RGrg][0-2]", "g0")),
                () -> assertFalse(Pattern.matches("[RGrg][0-2]", "0")),
                () -> assertFalse(Pattern.matches("[RGrg][0-2]", "G")),
                () -> assertFalse(Pattern.matches("[RGrg][0-2]", "2g")),
                () -> assertTrue(Pattern.matches("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen","Blue")),
                () -> assertTrue(Pattern.matches("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen","Green")),
                () -> assertTrue(Pattern.matches("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen","yellow")),
                () -> assertTrue(Pattern.matches("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen","red")),
                () -> assertFalse(Pattern.matches("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen","R"))
        );
    }

    @Test
    void patternGroupTest(){
        Matcher matcher = Pattern.compile("[RGrg][0-2]").matcher("place g0 on g1 TL");

        MatchResult result = matcher.toMatchResult();
        System.out.println("Current matcher: " + result);

        matcher.find();
        System.out.println("Match Found!!");
        assertEquals("g0",
                matcher.group()
        );
    }
}