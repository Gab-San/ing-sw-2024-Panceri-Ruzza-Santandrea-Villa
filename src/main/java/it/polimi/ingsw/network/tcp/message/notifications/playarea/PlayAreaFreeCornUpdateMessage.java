package it.polimi.ingsw.network.tcp.message.notifications.playarea;

import it.polimi.ingsw.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This class inherits from player message and represents a tcp message. Carries information about
 * free corners updates in the player area.
 */
public class PlayAreaFreeCornUpdateMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = -819507298374102L;
    private final List<SerializableCorner> freeSerializableCorners;

    /**
     * Constructs the free corner update message.
     * @param nickname play area's owner nickname
     * @param freeSerializableCorners a list of the free corners
     */
    public PlayAreaFreeCornUpdateMessage(String nickname, List<SerializableCorner> freeSerializableCorners) {
        super(nickname);
        this.freeSerializableCorners = freeSerializableCorners;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.freeCornersUpdate(nickname, freeSerializableCorners);
    }
}
