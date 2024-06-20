package it.polimi.ingsw.model.listener.remote.events.playarea;

import it.polimi.ingsw.SerializableCorner;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a player event. It is triggered by an update of the free corners.
 */
public class FreeCornersUpdate extends PlayerEvent  {
    private final List<SerializableCorner> freeSerializableCorners;

    /**
     * Constructs the free corners' event.
     * @param nickname play area owner's id
     * @param freeCorners updated free corners list
     */
    public FreeCornersUpdate(String nickname, List<Corner> freeCorners) {
        super(nickname);
        freeSerializableCorners = new LinkedList<>(freeCorners.stream().map(
                corner -> new SerializableCorner(corner.getCardRef().getCardID(),
                        corner.getDirection().toString())
        ).toList());
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.freeCornersUpdate(nickname, freeSerializableCorners);
    }
}
