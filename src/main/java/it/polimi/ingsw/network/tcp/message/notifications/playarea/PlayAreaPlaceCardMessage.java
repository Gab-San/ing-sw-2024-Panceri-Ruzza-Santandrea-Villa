package it.polimi.ingsw.network.tcp.message.notifications.playarea;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp server message interface. Carries information about a placed card.
 */
public class PlayAreaPlaceCardMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 45876519782L;
    private final String placedCardId;
    private final boolean placeOnFront;
    private final int row;
    private final int col;

    /**
     * Constructs place card message.
     * @param nickname play area's owner id
     * @param placedCardId identifier of the placed card
     * @param row x placement position
     * @param col y placement position
     * @param placeOnFront true if card face is up, false otherwise
     */
    public PlayAreaPlaceCardMessage(String nickname, String placedCardId, int row, int col, boolean placeOnFront) {
        super(nickname);
        this.placedCardId = placedCardId;
        this.placeOnFront = placeOnFront;
        this.row = row;
        this.col = col;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlaceCard(nickname, placedCardId, row, col, placeOnFront);
    }
}
