package it.polimi.ingsw;

import it.polimi.ingsw.CornerDirection;

import java.io.Serializable;

public record SerializableCorner(String cardCornerId, String cornerDirection) implements Serializable {
    public CornerDirection getCornerDirection(){
        return CornerDirection.getDirectionFromString(cornerDirection);
    }
}
