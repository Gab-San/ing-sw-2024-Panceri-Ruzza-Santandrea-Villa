package it.polimi.ingsw;

import it.polimi.ingsw.CornerDirection;

import java.io.Serializable;
import java.util.Map;

public record CardPosition(int row, int col, String cardId, boolean isFaceUp, Map<CornerDirection, Boolean> isCornerVisible) implements Serializable {

}
