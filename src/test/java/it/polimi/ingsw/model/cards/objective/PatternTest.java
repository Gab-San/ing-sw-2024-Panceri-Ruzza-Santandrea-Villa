package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static it.polimi.ingsw.model.enums.CornerDirection.*;
import static it.polimi.ingsw.model.enums.GameResource.*;
import static org.junit.jupiter.api.Assertions.*;

public class PatternTest {
    @Test
    void pattern_DIAG_BLUE_Test(){
        PatternObjective pattern = PatternObjective.DIAG_BLUE;
        Map<Point, GameResource> map = pattern.getPattern();
        Point center = new Point(0,0);

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
        PatternObjective pattern = PatternObjective.L_RED_RED_GREEN;
        Map<Point, GameResource> map = pattern.getPattern();
        Point center = new Point(0,0);

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
