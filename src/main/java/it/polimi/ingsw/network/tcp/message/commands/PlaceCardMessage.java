package it.polimi.ingsw.network.tcp.message.commands;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp client message interface. Sent if a place play card action is issued.
 */
public class PlaceCardMessage implements TCPClientMessage {
    @Serial
    private static final long serialVersionUID = 7382L;
    private final String nickname;
    private final String cardID;
    private final int xPosition;
    private final int yPosition;
    private final String cornDirection;
    private final boolean placeOnFront;

    /**
     * Constructs the place card message.
     * @param nickname unique id of the user
     * @param cardID identifier of the placed card
     * @param xPosition x-axis position of the card on which to place
     * @param yPosition y-axis position of the card on which to place
     * @param cornDirection corner direction of the corner on which to place
     * @param placeOnFront true if card must face upwards, false otherwise
     */
    public PlaceCardMessage(String nickname, String cardID,
                            int xPosition, int yPosition, String cornDirection, boolean placeOnFront) {
        this.nickname = nickname;
        this.cardID = cardID;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.cornDirection = cornDirection;
        this.placeOnFront = placeOnFront;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.placeCard(nickname, virtualClient, cardID,
                xPosition, yPosition, cornDirection, placeOnFront);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
