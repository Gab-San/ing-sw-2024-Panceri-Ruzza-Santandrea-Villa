package it.polimi.ingsw.network.tcp.message.notifications.playarea;

import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.List;

public class PlayAreaFreeCornUpdateMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = -819507298374102L;
    private final List<SerializableCorner> freeSerialableCorners;
    public PlayAreaFreeCornUpdateMessage(String nickname, List<SerializableCorner> freeSerialableCorners) {
        super(nickname);
        this.freeSerialableCorners = freeSerialableCorners;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {

    }
}
