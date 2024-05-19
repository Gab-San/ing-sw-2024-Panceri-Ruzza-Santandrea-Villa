package it.polimi.ingsw.model.listener.remote.events.playarea;

import it.polimi.ingsw.model.enums.CornerDirection;

import java.io.Serializable;

public record SerializableCorner(String cardCornerId, String cornerDirection) implements Serializable {
    public CornerDirection getCornerDirection(){
        return CornerDirection.getDirectionFromString(cornerDirection);
    }
}
