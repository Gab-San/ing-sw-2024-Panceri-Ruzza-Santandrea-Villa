package it.polimi.ingsw;

import java.io.Serializable;

/**
 * Serializable record representing the key information of a corner in the freeCorners list.
 * @param cardCornerId card ID of the card this corner is attached to.
 * @param cornerDirection the direction of this corner, as its toString()
 */
public record SerializableCorner(String cardCornerId, String cornerDirection) implements Serializable {
    /**
     * Unpacks the serialized information about the corner direction.
     * @return corner direction
     */
    public CornerDirection getCornerDirection(){
        return CornerDirection.getDirectionFromString(cornerDirection);
    }
}
