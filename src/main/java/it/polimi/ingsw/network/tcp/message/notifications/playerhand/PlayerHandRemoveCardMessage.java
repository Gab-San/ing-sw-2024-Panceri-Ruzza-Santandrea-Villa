package it.polimi.ingsw.network.tcp.message.notifications.playerhand;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class is a player message. Carries information about a removed card from the player's hand.
 */
public class PlayerHandRemoveCardMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 5784379812897L;
    private final String removedCardId;

    /**
     * Constructs the player hand remove card message.
     * @param nickname associated player's id
     * @param removedCardId removed card's id
     */
    public PlayerHandRemoveCardMessage(String nickname, String removedCardId) {
        super(nickname);
        this.removedCardId = removedCardId;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandRemoveCard(nickname, removedCardId);
    }
}
