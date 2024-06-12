package it.polimi.ingsw.network.tcp.message.notifications.playarea;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.CardPosition;
import it.polimi.ingsw.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * This class implements tcp server message interface.
 * Carries information about play area state.
 */
public class PlayAreaStateMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 157028937190L;
    private final List<CardPosition> cardPositions;
    private final Map<GameResource, Integer> visibleResources;
    private final List<SerializableCorner> freeSerializableCorners;

    /**
     * Constructs the play area state message.
     * @param nickname play area's owner id
     * @param cardPositions list of placed cards
     * @param visibleResources visible resources on the play area
     * @param freeSerializableCorners list of free corners
     */
    public PlayAreaStateMessage(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) {
        super(nickname);
        this.cardPositions = cardPositions;
        this.visibleResources = visibleResources;
        this.freeSerializableCorners = freeSerializableCorners;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.setPlayAreaState(nickname, cardPositions, visibleResources, freeSerializableCorners);
    }
}
