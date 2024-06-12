package it.polimi.ingsw.network.tcp.message.notifications.playerhand;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class represents a player message. It notifies about the starting card given to the player.
 */
public class PlayerHandSetStartingCardMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = -84684359874354L;
    private final String startingCardId;

    /**
     * Constructs the starting card message.
     * @param nickname player's nickname.
     * @param startingCardId identifier of the starting card given to the player
     */
    public PlayerHandSetStartingCardMessage(String nickname, String startingCardId) {
        super(nickname);
        this.startingCardId = startingCardId;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandSetStartingCard(nickname, startingCardId);
    }
}
