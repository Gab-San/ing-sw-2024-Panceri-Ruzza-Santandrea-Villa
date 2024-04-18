package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;
import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.Hashtable;
import java.util.Map;

import static it.polimi.ingsw.model.enums.CornerDirection.TL;
import static it.polimi.ingsw.model.enums.CornerDirection.TR;
import static it.polimi.ingsw.model.enums.CornerDirection.BL;
import static it.polimi.ingsw.model.enums.CornerDirection.BR;

// String pattern convention:
// * = any card   ;  R,G,B,P = Red, Green, Blue, Purple card
// corner positions are TL, TR, BR, BL to the center card
// the positions between corners are the top, bottom, left, right cards
// e.g. to get the top card it's Point(0,0).move(TL, TR)
// e.g. to get the left card it's Point(0,0).move(TL, BL)
// TODO: change PatternObjective from hard-coded enum to JSON import
public enum PatternObjective {
    L_RED_RED_GREEN("*R*" +
                    "*R*" +
                    "**G"),
    DIAG_BLUE(  "**B" +
                "*B*" +
                "B**")
    ;

    final Map<Point, GameResource> pattern;
    PatternObjective(@NotNull String strPattern){
        pattern = new Hashtable<>();

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

    public Map<Point, GameResource> getPattern(){
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
    //TODO: Improve translation from pattern string position to Point
    private Point patternPosToPoint(int pos) throws InvalidParameterException{
        Point center = new Point(0,0);
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
}
