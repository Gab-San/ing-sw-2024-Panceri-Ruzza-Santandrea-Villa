package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.GameResource;
import org.jetbrains.annotations.*;
import java.security.InvalidParameterException;
import static it.polimi.ingsw.CornerDirection.TL;
import static it.polimi.ingsw.CornerDirection.TR;
import static it.polimi.ingsw.CornerDirection.BL;
import static it.polimi.ingsw.CornerDirection.BR;
import java.util.*;

/**
 * Pattern for objective cards that require it
 * <br> <br>
 * String pattern convention:
 * <ul>
 *     <li>
 *          * = any card <br>
 *          R,G,B,P = Red, Green, Blue, Purple card
 *     </li>
 *     <li>
 *          corner positions are TL, TR, BR, BL to the center card
 *     </li>
 *     <li>
 *          the positions between corners are the top, bottom, left, right cards
 *     </li>
 *     <li>
 *          e.g. to get the top card it's Point(0,0).move(TL, TR)
 *     </li>
 *     <li>
 *          e.g. to get the left card it's Point(0,0).move(TL, BL)
 *     </li>
 * </ul>
 */

public class PatternObjective {
    final Map<GamePoint, GameResource> pattern;
    final String initialString;

    /**
     * Constructs pattern from string
     * @param strPattern example format: "G** *G* **G" <br> where the first 3 chars are the top row,
     *                  <br> the middle 3 chars are the middle row
     *                  <br> the last 3 chars are the bottom row
     */
    public PatternObjective(@NotNull String strPattern){
        pattern = new Hashtable<>();
        initialString = strPattern;
        //removes the spaces (they were needed in the string for readability, but translation to map is easier without them
        strPattern = strPattern.replace(" ", "");

        for (int i = 0; i < strPattern.length(); i++) {
            try {
                pattern.put(
                        patternPosToPoint(i),
                        charToResource(strPattern.charAt(i))
                );
            }
            catch(InvalidParameterException ignored){}
            // '*' characters are ignored as whatever is in those points is irrelevant
            // strings longer than 9 (3x3) are also ignored (might want to allow for custom patterns, e.g. 5x5)
        }
    }

    /**
     * Returns objective pattern as a map.
     * @return pattern as a map similar to PlayArea.cardMatrix (points are centered in (0,0))
     */
    public Map<GamePoint, GameResource> getPattern(){
        return pattern;
    }
    private GameResource charToResource(char c) throws InvalidParameterException{
        return switch (c) {
            case 'R' -> GameResource.MUSHROOM; // red
            case 'B' -> GameResource.WOLF; // blue
            case 'G' -> GameResource.LEAF; // green
            case 'P' -> GameResource.BUTTERFLY; // purple
            default ->  throw new InvalidParameterException();
        };
    }

    private GamePoint patternPosToPoint(int pos) throws InvalidParameterException{
        GamePoint center = new GamePoint(0,0);
        return switch (pos){
            case 0 -> center.move(TL);
            case 1 -> center.move(TL, TR);
            case 2 -> center.move(TR);
            case 3 -> center.move(TL, BL);
            case 4 -> center;
            case 5 -> center.move(TR, BR);
            case 6 -> center.move(BL);
            case 7 -> center.move(BL, BR);
            case 8 -> center.move(BR);
            default -> throw new InvalidParameterException();
        };
    }

    @Override
    public String toString() {
        return initialString;
    }
}
