package it.polimi.ingsw.network.tcp.message.notifications.playerhand;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class inherits from player message. Carries information about added card to the player hand.
 */
public class PlayerHandAddCardMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 8751098237L;
    private final String drawnCardId;

    /**
     * Constructs the hand added card message.
     * @param nickname associated player's unique id
     * @param drawnCardId unique identifier of the card added to the hand
     */
    public PlayerHandAddCardMessage(String nickname, String drawnCardId) {
        super(nickname);
        this.drawnCardId = drawnCardId;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.playerHandAddedCardUpdate(nickname, drawnCardId);
    }
}
