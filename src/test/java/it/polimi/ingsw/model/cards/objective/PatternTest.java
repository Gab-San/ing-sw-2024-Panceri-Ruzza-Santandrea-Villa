package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.GameResource;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.GameResource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PatternTest {
    @Test
    void pattern_StringToPattern_Test(){
        GamePoint center = new GamePoint(0,0);
        PatternObjective diagBLUE = new PatternObjective("**B *B* B**");
        assertEquals(WOLF, diagBLUE.getPattern().get(center.move(TR)));
        assertEquals(WOLF, diagBLUE.getPattern().get(center));
        assertEquals(WOLF, diagBLUE.getPattern().get(center.move(BL)));
        PatternObjective L_RED_GREEN = new PatternObjective("*R* *R* **G");
        assertEquals(MUSHROOM, L_RED_GREEN.getPattern().get(center.move(TR, TL)));
        assertEquals(MUSHROOM, L_RED_GREEN.getPattern().get(center));
        assertEquals(LEAF, L_RED_GREEN.getPattern().get(center.move(BR)));
        PatternObjective revDiagPURPLE = new PatternObjective("P** *P* **P");
        assertEquals(BUTTERFLY, revDiagPURPLE.getPattern().get(center.move(TL)));
        assertEquals(BUTTERFLY, revDiagPURPLE.getPattern().get(center));
        assertEquals(BUTTERFLY, revDiagPURPLE.getPattern().get(center.move(BR)));
        PatternObjective rev_L_BLUE_RED = new PatternObjective("*B* *B* R**");
        assertEquals(WOLF, rev_L_BLUE_RED.getPattern().get(center.move(TR, TL)));
        assertEquals(WOLF, rev_L_BLUE_RED.getPattern().get(center));
        assertEquals(MUSHROOM, rev_L_BLUE_RED.getPattern().get(center.move(BL)));
    }
    @Test
    void pattern_DIAG_BLUE_Test(){
        PatternObjective pattern = new PatternObjective("**B *B* B**");
        Map<GamePoint, GameResource> map = pattern.getPattern();
        GamePoint center = new GamePoint(0,0);

        assertEquals(WOLF, map.get(center));
        assertEquals(WOLF, map.get(center.move(TR)));
        assertEquals(WOLF, map.get(center.move(BL)));
        assertNull(map.get(center.move(BR)));
        assertNull(map.get(center.move(TL)));
        assertNull(map.get(center.move(TL, TR))); // top
        assertNull(map.get(center.move(TL, BL))); // left
        assertNull(map.get(center.move(TR, BR))); // right
        assertNull(map.get(center.move(BL, BR))); // bottom
    }
    @Test
    void pattern_L_RED_RED_GREEN_Test(){
        PatternObjective pattern = new PatternObjective("*R* *R* **G");
        Map<GamePoint, GameResource> map = pattern.getPattern();
        GamePoint center = new GamePoint(0,0);

        assertEquals(MUSHROOM, map.get(center));
        assertEquals(LEAF, map.get(center.move(BR)));
        assertEquals(MUSHROOM, map.get(center.move(TR, TL))); // top
        assertNull(map.get(center.move(TR)));
        assertNull(map.get(center.move(BL)));
        assertNull(map.get(center.move(TL)));
        assertNull(map.get(center.move(TL, BL))); // left
        assertNull(map.get(center.move(TR, BR))); // right
        assertNull(map.get(center.move(BL, BR))); // bottom
    }
}
