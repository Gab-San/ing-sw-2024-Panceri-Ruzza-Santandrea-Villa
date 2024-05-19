package it.polimi.ingsw.model.listener.remote.events.playarea;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

public class FreeCornersUpdate extends PlayerEvent {
    private final List<SerializableCorner> freeSerialableCorners;
    public FreeCornersUpdate(String nickname, List<Corner> freeCorners) {
        super(nickname);
        freeSerialableCorners = new LinkedList<>(freeCorners.stream().map(
                corner -> new SerializableCorner(corner.getCardRef().getCardID(),
                        corner.getDirection().toString())
        ).toList());
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.freeCornersUpdate(nickname, freeSerialableCorners);
    }
}
